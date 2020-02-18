package pwd.allen.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * 开启spring mvc异步支持
 *  方式一：xml配置中，相关的配置组件中加入<async-supported>true</async-supported>
 *  方式二：java bean方式，setAsyncSupported(true)
 * 注意：不只是DispatcherServlet需要设置开启异步支持，调用到的filter也需要设置
 *
 * @author 门那粒沙
 * @create 2020-02-12 21:26
 **/
@Log4j2
@RequestMapping("async")
@RestController
public class AsyncController {

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * 使用DeferredResult作为返回值 实现异步处理逻辑
     * 需要自己来处理返回结果（正常返回调setResult；异常时调setErrorResult，不调用的话会等待直到asyncTimeOut），一般做法是维护一个结果处理队列，然后用其他线程来不断处理
     * 相较于Callable来说 ，较麻烦，但是灵活
     *
     * @return
     */
    @GetMapping("deferredResult")
    public DeferredResult<String> deferredResult() {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                log.info("【DeferredResult】异步操作已完成");
            }
        });
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                log.info("【DeferredResult】异步操作超时");
            }
        });

        //模拟其他线程异步调用
        new Thread(() -> {
            //设置返回结果
            deferredResult.setResult("Hello!");
            //这里是无效的，第一次setResult的时候已经返回结果完毕
            deferredResult.setResult("Hello!");
        }).start();

        return deferredResult;
    }

    /**
     * 使用DeferredResult作为返回值 实现异步处理逻辑
     *
     * @return
     */
    @GetMapping("callable")
    public Callable<String> callable() {
        return () -> {
            Thread.sleep(1000);
            return "hello! i am testing callable";
        };
    }

    /**
     * 使用ResponseBodyEmitter作为返回值 实现异步处理逻辑
     * 以流的形式响应
     *
     * @return
     */
    @GetMapping("emitter")
    public ResponseBodyEmitter responseBodyEmitter() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(1000l * 5);

        emitter.onCompletion(() -> log.info("【ResponseBodyEmitter】异步操作完成"));

        //模拟在其他线程异步操作
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                emitter.send("i am message one!");
                emitter.send("i am message two!");
                emitter.complete();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return emitter;
    }


    /**
     * 使用StreamingResponseBody 直接异步发送原生流数据
     *
     * @return
     */
    @GetMapping("stream")
    public StreamingResponseBody steam() {
        return outputStream -> {
            log.info("【StreamingResponseBody】异步操作");
            outputStream.write("StreamingResponseBody".getBytes());
        };
    }


    /**
     * 调用异步方法
     * @return
     */
    @GetMapping("annotation")
    public Object annotation() {
        AsyncController bean = applicationContext.getBean(AsyncController.class);
        bean.execute();
        return "finish!";
    }
    /**
     * 需要在bean容器中，且要开启基于注解的异步功能@EnableAsync
     */
    @Async
    public void execute() {
        log.info("【@Async】异步操作");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
