package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import pwd.allen.batch.*;
import pwd.allen.config.BatchConfig;
import pwd.allen.config.BatchConfig2;
import pwd.allen.config.DBConfig;
import pwd.allen.entity.Person;
import pwd.allen.exception.MyException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 门那粒沙
 * @create 2020-02-23 14:53
 **/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DBConfig.class, BatchConfig.class, BatchConfig2.class})
public class BatchTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobRepository jobRepository;

    /**
     * 这个不是@EnableBatchProcessing自动引入的，需要自己创建
     * 其中设置的jobRegistry需要通过JobRegistryBeanPostProcessor来注册job，然后才能通过beanName找到job
     */
    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Job myJob;

    @Autowired
    private FlatFileItemReader<Person> flatFileItemReader;

    //<editor-fold desc="数据库初始化">
    /**
     * 初始化spring batch的数据库环境
     */
    @Test
    @Sql(scripts = {"classpath:org/springframework/batch/core/schema-drop-mysql.sql", "classpath:org/springframework/batch/core/schema-mysql.sql"}, config = @SqlConfig(commentPrefix = "--", separator = ";"))
    public void init() {}
    //</editor-fold>

    /**
     * 以yyyy-MM-dd作为参数启动job，测试一天内启动多次（会抛出JobInstanceAlreadyCompleteException）
     * 在同一个线程执行
     * @throws JobParametersInvalidException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     * @throws IOException
     */
    @Test
    public void test() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {
        //创建job参数
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()))
                .toJobParameters();

        //创建tasklet步骤
        TaskletStep step1 = stepBuilderFactory.get("step1").tasklet(new MyTasklet("第一个任务啦")).build();
        TaskletStep step2 = stepBuilderFactory.get("step2").tasklet(new MyTasklet("第二个任务啦")).build();
        //创建一个chunk步骤，为String集合加上前缀
        TaskletStep step3 = stepBuilderFactory.get("chunkStep")
                .<String, String>chunk(2)
                .reader(new ListItemReader<String>(Arrays.asList("a", "b", "c")))
                .processor(o -> "hello!" + o)
                .writer(list -> System.out.println("write " + list))
                .listener(new MyChunkListener())
                .build();

        //创建flow，flow能像job一样组织step，能重用
        Flow flow1 = new FlowBuilder<Flow>("flow1").start(step1)
                .next(step2)
                .build();

        //用job组织step
        //创建包含job的step，实现子job，即job内嵌job
        Step jobStep = new JobStepBuilder(new StepBuilder("jobStep"))
                .job(myJob)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
        Job job1 = jobBuilderFactory.get("job1")
                .start(flow1)
                .on(ExitStatus.COMPLETED.getExitCode()).to(step3)//当返回结果为COMPLETED时走step3
                .from(step3).on(ExitStatus.COMPLETED.getExitCode()).to(jobStep)//当step3结果为COMPLETED时执行 子job
                .end()
                .listener(new MyJobExecutionListener())//注册job监听器
                .build();

        //阻塞
        jobLauncher.run(job1, jobParameters);
    }


    /**
     * 测试split并行执行
     */
    @Test
    public void split() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .toJobParameters();

        TaskletStep step1 = stepBuilderFactory.get("step1").tasklet(new MyTasklet("第一个任务啦")).build();
        TaskletStep step2 = stepBuilderFactory.get("step2").tasklet(new MyTasklet("第二个任务啦")).build();
        TaskletStep step3 = stepBuilderFactory.get("step3").tasklet(new MyTasklet("第3个任务啦")).build();

        //step1和step2串行，但是与step3并行
        Flow flow1 = new FlowBuilder<Flow>("flow1").start(step1)
                .next(step2)
                .build();
        Flow flow2 = new FlowBuilder<Flow>("flow2").start(step3)
                .build();

        Job job1 = jobBuilderFactory.get("splitJob")
                .start(flow1)
                .split(new SimpleAsyncTaskExecutor())
                .add(flow2)
                .end()
                .build();

        //阻塞
        jobLauncher.run(job1, jobParameters);
    }

    /**
     * 测试决策器，通过决策器控制步骤执行
     * @throws JobParametersInvalidException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     */
    @Test
    public void decide() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .addString("decide", "step1")
                .toJobParameters();

        TaskletStep step1 = stepBuilderFactory.get("step1").tasklet(new MyTasklet("第一个任务啦")).build();
        TaskletStep step2 = stepBuilderFactory.get("step2").tasklet(new MyTasklet("第二个任务啦")).build();

        Flow flow1 = new FlowBuilder<Flow>("flow1").start(step1)
                .next(step2)
                .build();

        MyJobExecutionDecider executionDecider = new MyJobExecutionDecider();

        Job job1 = jobBuilderFactory.get("decideJob")
                .start(flow1)
                .next(executionDecider)
                .on("step1").to(step1)//如果决策器返回step1则执行step1
                .from(executionDecider).on("step2").to(step2)//如果决策器返回step1则执行step1
//                .from(executionDecider).on("*").to(flow1)//其他情况重新回到flow1，死循环
                .from(executionDecider).on("*").stop()
                .end()
                .build();

        //阻塞
        jobLauncher.run(job1, jobParameters);
    }

    /**
     * 测试失败容忍
     *  （1）重试：指定需要重试的异常类型，指定重试次数
     *  （2）跳过：指定需要跳过的异常类型，指定次数上限，跳过后整个chunk除了被跳过的item外，其他item会重新进行ItemProcessor处理
     */
    @Test
    public void retry() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()))
                .toJobParameters();

        TaskletStep step = stepBuilderFactory.get("step")
                .<Person, Person> chunk(10)
                .reader(flatFileItemReader)
                .processor(new TestItemProcessor())
                .writer(items -> System.out.println("write : " + items))
                .faultTolerant()//开启失败容忍
//                .retry(MyException.class)//遇到MyException则重试
//                .retryLimit(5)//重试次数
                .skip(MyException.class)//遇到MyException则跳过
                .skipLimit(2)//只能跳过一次，第二次遇到该异常则出错
                .listener(new MySkipListener())//加入一个监听器，监听跳过的过程
                .build();

        Job job = jobBuilderFactory.get("retryJob")
                .start(step)
                .build();

        //阻塞
        jobLauncher.run(job, jobParameters);
    }

    @Test
    public void jobOperator() throws JobParametersInvalidException, JobInstanceAlreadyExistsException, NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobParametersNotFoundException, JobInstanceAlreadyCompleteException {
        //打印容器里的job
        Set<String> jobNames = jobOperator.getJobNames();
        System.out.println(jobNames);

        //启动myJob
        jobOperator.startNextInstance(jobNames.iterator().next());
    }


    public class TestItemProcessor implements ItemProcessor<Person, Person> {
        private int attemptCount = 0;
        @Override
        public Person process(Person item) throws Exception {
            System.out.println("process item " + item.getId());
            List<Integer> ids = Arrays.asList(102, 106);
            //模拟数据处理出错，重试3次后成功的场景
            if (ids.contains(item.getId()) && attemptCount++ < 3) {
                System.out.println(String.format("fail to process for id:%s attempCount:%s", item.getId(), attemptCount));
                throw new MyException("dang late go fight!");
            }
            return item;
        }
    }

}
