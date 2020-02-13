package pwd.allen.exceptionHandler;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Collections;

/**
 * 定义公共的 @ExceptionHandler @InitBinder @ModelAttribute方法
 *
 *
 * @author 门那粒沙
 * @create 2020-02-12 11:57
 **/
@Log4j2
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * 当handler方法发生异常时 用来处理指定的异常
     * 可以使用Exception 作为入参，表示对应发生的异常对象
     *
     * 原理：{@link ExceptionHandlerExceptionResolver} 会识别@ExceptionHandler，
     *  当handler发生异常时，先找该handler是否有@ExceptionHandler标注的方法，若没有则寻找@ControllerAdvice中的@ExceptionHandler注解方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler({RuntimeException.class})
    public Object handleError(Exception e) {
       log.info("【默认的ExceptionHandler】error:{}", e.toString());
       return Collections.singletonMap("error", e.toString());
    }
}
