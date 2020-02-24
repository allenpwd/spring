package pwd.allen.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import pwd.allen.batch.MyLineAggregator;
import pwd.allen.batch.MyStepExecutionListener;
import pwd.allen.entity.Person;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;

/**
 * @author 门那粒沙
 * @create 2020-02-23 14:15
 **/
@Configuration
@EnableBatchProcessing
@ComponentScan("pwd.allen.batch")
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Value("classpath*:/*.csv")
    private Resource[] resources;

    @Bean
    public Job myJob(Step step) {
        return jobBuilderFactory.get("myJob")//指定工作名称
                .start(step)
                .build();
    }

    /**
     * 创建步骤
     * reader是逐个逐个执行的
     *  ItemStream：支持运行环境状态的存储与恢复
     *      open(executionContext)：通过该方法可以获取执行上下文的信息，然后恢复状态
     *      update(executionContext)：通过该方法可以在存储之前设置一些状态
     *      close()：释放资源用
     *  read()：读一条记录
     * writer是逐chunk逐chunk执行的
     *
     * @return
     */
    @Bean
    public Step myStep(MyStepExecutionListener myStepExecutionListener) throws Exception {
        return stepBuilderFactory.get("myStep")
                .<Person, Person> chunk(10)
//                .reader(multiResourceItemReader())
                .reader(flatFileItemReader())
//                .reader(staxEventItemReader())
//                .reader(jdbcPagingItemReader())//这里应该是会覆盖上面的reader，因为加上后只从jdbc中读数据
                .processor(item -> {
                    if ("error".equals(item.getUserName())) throw new RuntimeException("出错了！");
                    return item;
                })
//                .writer(jdbcBatchItemWriter())//数据插入数据库
                .writer(flatFileItemWriter())
//                .writer(itemWriter())//打印要写入的数据
                .listener(myStepExecutionListener)
                .build();
    }

    //<editor-fold desc="自定义ItemReader">
    /**
     * 创建从数据库读取数据的ItemReader
     * 从数据库db_user表中读取数据
     * @return
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<Person> jdbcPagingItemReader() {
        JdbcPagingItemReader<Person> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.dataSource);
        reader.setFetchSize(100);
        reader.setRowMapper(new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = new Person();
                person.setId(rs.getInt("id"));
                person.setAge(rs.getInt("age"));
                person.setUserName(rs.getString("user_name"));
                person.setCreateAt(rs.getDate("create_at"));
                return person;
            }
        });

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("from db_user");
        queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));
        reader.setQueryProvider(queryProvider);

        return reader;
    }

    /**
     * 创建从文本文件读取数据的ItemReader
     * 支持从出错的chunk开始处理，因为出错时的状态被持久化保存到batch_step_execution_context，根据executionContext记录的行数（FlatFileItemReader.read.count），前面的行是已经处理并且执行写入成功的
     *
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Person> flatFileItemReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("person.csv"));
        reader.setEncoding("GBK");
        reader.setLinesToSkip(1);//忽略第一行表头
        reader.setSkippedLinesCallback(line -> System.out.println("跳过：" + line));//跳过的行打印出来

        //创建 解析一行记录的分词器
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id", "userName", "age", "createAt"});
        //自定义日期格式，默认yyyy-MM-dd
//        DefaultFieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();
//        fieldSetFactory.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//        tokenizer.setFieldSetFactory(fieldSetFactory);

        //结果映射
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Person person = new Person(fieldSet.readInt("id"), fieldSet.readString("userName"), fieldSet.readInt("age"));
            person.setCreateAt(fieldSet.readDate("createAt"));
            return person;
        });
        lineMapper.afterPropertiesSet();

        reader.setLineMapper(lineMapper);


        return reader;
    }

    /**
     * 创建从xml文件读取数据的ItemReader
     * @return
     */
    @Bean
    @StepScope
    public StaxEventItemReader<Person> staxEventItemReader() {
        StaxEventItemReader<Person> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("person.xml"));

        //要解析的customer的根元素
        reader.setFragmentRootElementName("person");

        //设置xml反序列化工具
        XStreamMarshaller marshaller = new XStreamMarshaller();
        //person元素映射成Person类
        marshaller.setAliases(Collections.singletonMap("person", Person.class));
        reader.setUnmarshaller(marshaller);

        return reader;
    }

    /**
     * 可以读取多个resource的ItemReader
     * @return
     */
    @Bean
    @StepScope
    public MultiResourceItemReader<Person> multiResourceItemReader() {
        MultiResourceItemReader<Person> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setDelegate(flatFileItemReader());
        multiResourceItemReader.setResources(resources);

        return multiResourceItemReader;
    }
    //</editor-fold>

    //<editor-fold desc="自定义ItemWriter">
    /**
     * 写入数据库itemWriter
     *
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Person> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into db_user(id,user_name,age,create_at) values(:id,:userName,:age,:createAt)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        return itemWriter;
    }

    /**
     * 写入文本文件
     *
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<Person> flatFileItemWriter() throws Exception {
        FlatFileItemWriter<Person> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(new FileSystemResource("C:\\Users\\Administrator\\Desktop\\output.txt"));

        //设置行聚合器，将记录转成一行
        itemWriter.setLineAggregator(new MyLineAggregator());
        itemWriter.afterPropertiesSet();//自检

        return itemWriter;
    }

    /**
     * 自定义itemWriter
     * 逐chunk的数据集合进行写入
     * @return
     */
    @Bean
    public ItemWriter<Person> itemWriter() {
        return items -> {
            System.out.println("write " + items);
        };
    }
    //</editor-fold>
}
