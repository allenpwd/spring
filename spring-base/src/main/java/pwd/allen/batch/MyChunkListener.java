package pwd.allen.batch;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * 自定义chunk监听器
 */
public class MyChunkListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        System.out.println("【chunk监听器】beforeChunk " + chunkContext.isComplete());
    }
    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println("【chunk监听器】afterChunk " + chunkContext.isComplete());
    }
    @AfterChunkError
    public void afterChunkError(ChunkContext chunkContext) {
        System.out.println("【chunk监听器】afterChunkError " + chunkContext.isComplete());
    }
}
