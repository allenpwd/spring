package pwd.allen.batch;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * 步骤执行监听器
 * @author 门那粒沙
 * @create 2020-02-23 14:23
 **/
@Log4j2
@Component
public class MyStepExecutionListener extends StepExecutionListenerSupport {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("wrote：{}；stepName：{}", stepExecution.getWriteCount(), stepExecution.getStepName());
        return null;
    }
}
