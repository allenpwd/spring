package pwd.allen.batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

/**
 * JobParameter生成器
 *
 * @author 门那粒沙
 * @create 2020-02-25 14:39
 **/
public class MyJobParametersIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters jobParameters = parameters == null ? new JobParameters() : parameters;
        return new JobParametersBuilder(jobParameters)
                .addString("name", "pwd")
                .toJobParameters();
    }
}
