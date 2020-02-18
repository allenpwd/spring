package pwd.allen.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * 自定义监听器，监听ApplicationEvent及其子类事件
 * @author pwd
 * @create 2018-12-01 18:13
 **/
@Component
@Log4j2
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
    
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
        log.debug("ApplicationListener接口实现接受事件：" + event);
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
        log.debug("@EventListener接收事件：" + event);
    }

    /**
     * 监听requestUrl包含my.do的请求
     * @param event
     */
    @EventListener(classes = {ServletRequestHandledEvent.class}, condition = "#event.requestUrl.contains('my.do')")
    public void myEventListenerMethodTwo(ServletRequestHandledEvent event) {
        log.debug("@EventListener接收事件：" + event);
    }

    /**
     * 监听事务
     *
     * 注解 {@link TransactionalEventListener}（需要Spring4.2+）
     * 属性：
     *  phase:监听事务的哪个阶段，默认提交后 {@link TransactionPhase#AFTER_COMMIT}
     *  fallbackExecution:为true则没有事务时也会触发，默认false
     *
     * 原理：
     *  通过 {@link org.springframework.transaction.event.TransactionalEventListenerFactory} 把标注有 @TransactionalEventListener注解的方法最终都包装成一个ApplicationListenerMethodTransactionalAdapter，它是一个ApplicationListener，最终注册进事件发射器的容器里面
     *  监听的事件到来时ApplicationListenerMethodTransactionalAdapter判断是否在事务中（若fallbackExecution=true，那就是表示即使没有事务  也会执行handler），是的话向事务管理器注册一个事务同步器 TransactionSynchronizationEventAdapter
     *
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleTransaction(ApplicationEvent event) {
        log.debug("事务回滚了：" + event);
    }
}
