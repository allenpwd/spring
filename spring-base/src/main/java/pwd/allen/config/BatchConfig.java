package pwd.allen.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import pwd.allen.batch.MyStepExecutionListener;
import pwd.allen.entity.Person;

import javax.sql.DataSource;
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

    @Bean
    public Job myJob(Step step) {
        return jobBuilderFactory.get("myJob")//指定工作名称
                .start(step)
                .build();
    }

    /**
     * 创建步骤：从数据库db_user表中读取数据并打印
     * @return
     */
    @Bean
    public Step myStep(MyStepExecutionListener myStepExecutionListener) {
        return stepBuilderFactory.get("myStep")
                .<Person, Person> chunk(100)
                .reader(flatFileItemReader())
//                .reader(jdbcPagingItemReader())//这里应该是会覆盖上面的reader，因为加上后只从jdbc中读数据
                .writer(list -> System.out.println("write " + list))
                .listener(myStepExecutionListener)
                .build();
    }

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
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Person> flatFileItemReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("person.csv"));
        reader.setEncoding("GBK");
        reader.setLinesToSkip(1);//忽略第一行表头

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
}
