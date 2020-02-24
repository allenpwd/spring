package pwd.allen.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * 自定义step的处理程序
 * @author 门那粒沙
 * @create 2020-02-24 12:53
 **/
public class MyTasklet implements Tasklet {

    private String message;

    private boolean ifThrowError = false;

    public MyTasklet(String message) {
        this.message = message;
    }

    public MyTasklet error() {
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