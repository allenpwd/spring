package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.FlowStepBuilder;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import pwd.allen.config.BatchConfig;
import pwd.allen.config.DBConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 门那粒沙
 * @create 2020-02-23 14:53
 **/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchConfig.class, DBConfig.class})
public class BatchTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private Job job;

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

        //创建第一个步骤
        TaskletStep step1 = stepBuilderFactory.get("step1").tasklet(new TestTasklet("第一个任务啦")).build();
        TaskletStep step2 = stepBuilderFactory.get("step2").tasklet(new TestTasklet("第二个任务啦")).build();
        TaskletStep step3 = stepBuilderFactory.get("step3").tasklet(new TestTasklet("第3个任务啦")).build();

        //创建flow，flow能像job一样组织step，能重用
        Flow flow1 = new FlowBuilder<Flow>("flow1").start(step1)
                .next(step2)
                .build();
        Flow flow2 = new FlowBuilder<Flow>("flow2").start(step3)
                .build();

        //job包装step
        Job job2 = jobBuilderFactory.get("job2").start(step3).build();
        new JobStepBuilder(new StepBuilder("jobStep"))
                .job(job2)
                .launcher(jobLauncher)
                .repository(jobRepository)
        Job job1 = jobBuilderFactory.get("job1")
                .start(flow1)
                .on(ExitStatus.COMPLETED.getExitCode()).to(step3)//当返回结果为COMPLETED时走step2
                .from(step3).on(ExitStatus.COMPLETED.getExitCode()).to(step3)
                .end()
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

        TaskletStep step1 = stepBuilderFactory.get("step1").tasklet(new TestTasklet("第一个任务啦")).build();
        TaskletStep step2 = stepBuilderFactory.get("step2").tasklet(new TestTasklet("第二个任务啦")).build();
        TaskletStep step3 = stepBuilderFactory.get("step3").tasklet(new TestTasklet("第3个任务啦")).build();

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

    @Test
    public void decide() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .addString("decide", "step1")
                .toJobParameters();

        TaskletStep step1 = stepBuilderFactory.get("step1").tasklet(new TestTasklet("第一个任务啦")).build();
        TaskletStep step2 = stepBuilderFactory.get("step2").tasklet(new TestTasklet("第二个任务啦")).build();

        Flow flow1 = new FlowBuilder<Flow>("flow1").start(step1)
                .next(step2)
                .build();

        TestJobExecutionDecider executionDecider = new TestJobExecutionDecider();

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
     * 自定义step的处理程序
     */
    public class TestTasklet implements Tasklet {

        private String message;

        private boolean ifThrowError = false;

        public TestTasklet(String message) {
            this.message = message;
        }

        public TestTasklet error() {
            this.ifThrowError = true;
            return this;
        }

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            System.out.println(String.format("%s in thread %s ：%s"
                    , chunkContext.getStepContext().getStepName()
                    , Thread.currentThread().getName(), message));
            if (ifThrowError) throw new RuntimeException("啊 我错了");
            return doExecute(contribution, chunkContext);
        }

        public RepeatStatus doExecute(StepContribution contribution, ChunkContext chunkContext) {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            return RepeatStatus.CONTINUABLE;//会一直重复执行
            return RepeatStatus.FINISHED;
        }
    }

    /**
     * 自定义决策器
     */
    public class TestJobExecutionDecider implements JobExecutionDecider {

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            //简单地把参数中的decide作为返回值
            return new FlowExecutionStatus(jobExecution.getJobParameters().getString("decide"));
        }
    }
}
