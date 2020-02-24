package pwd.allen.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * 自定义job监听器
 */
public class MyJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("【job监听器】beforeJob " + jobExecution.getJobInstance().getJobName());
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("【job监听器】afterJob " + jobExecution.getJobInstance().getJobName());
    }
}
