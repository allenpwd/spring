package pwd.allen.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 门那粒沙
 * @create 2020-02-23 14:15
 **/
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job job(Step step) {
        return jobBuilderFactory.get("myJob")//指定工作名称
                .start(step)
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("myStep").tasklet((contribution, chunkContext) -> {
            System.out.println("这是处理逻辑");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
