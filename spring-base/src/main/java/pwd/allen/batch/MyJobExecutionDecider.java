package pwd.allen.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * 自定义决策器
 *
 * @author 门那粒沙
 * @create 2020-02-24 12:55
 **/
public class MyJobExecutionDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        //简单地把参数中的decide作为返回值
        return new FlowExecutionStatus(jobExecution.getJobParameters().getString("decide"));
    }
}