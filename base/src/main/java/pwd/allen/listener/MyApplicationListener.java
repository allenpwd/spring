package pwd.allen.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * 自定义监听器，监听ApplicationEvent及其子类事件
 * @author pwd
 * @create 2018-12-01 18:13
 **/
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(MyApplicationListener.class);

    /**
     * 发布ContextRefreshedEvent事件
     *  1）、容器创建对象、refresh()
     *  2）、finishRefresh();容器刷新完成
     *  3）、publishEvent(new ContextRefreshedEvent(this));
     *
     *
     * 事件发布流程publishEvent(event)：
     *   -》获取派发器getApplicationEventMulticaster()-》派发器获取所有ApplicationListener;如果有Executor,可以支持使用Executor进行异步派发，否则同步派发
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        logger.debug("ApplicationListener接口实现接受事件：" + event);
    }

    /**
     * 使用 {@link EventListener}注解监听事件
     * 原理：
     *  使用 {@link org.springframework.context.event.EventListenerMethodProcessor}处理器来解析方法上的@EventListener，将方法注册为一个ApplicationListener的instance
     *  该处理器实现了 {@link org.springframework.beans.factory.SmartInitializingSingleton}接口，会在所有单例创建后执行
     *
     * @param event
     */
    @EventListener(classes = {ApplicationEvent.class})
    public void myEventListenerMethod(ApplicationEvent event) {
        logger.debug("@EventListener接收事件：" + event);
    }

    /**
     * 监听requestUrl包含my.do的请求
     * @param event
     */
    @EventListener(classes = {ServletRequestHandledEvent.class}, condition = "#event.requestUrl.contains('my.do')")
    public void myEventListenerMethodTwo(ServletRequestHandledEvent event) {
        logger.debug("@EventListener接收事件：" + event);

    }
}