package pwd.allen.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author pwd
 * @create 2018-12-01 18:13
 **/
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

    /**
     * 原理
     * 1）、ContextRefreshedEvent事件
     *  1）、容器创建对象、refresh()
     *  2）、finishRefresh();容器刷新完成
     *  3）、publishEvent(new ContextRefreshedEvent(this));
     *      事件发布流程：
     *          1）获取事件的派发器：getApplicationEventMulticaster()
     *          2）multicastEvent派发事件
     *          3）获取所有ApplicationListener;如果有Executor,可以支持使用Executor进行异步派发，否则同步派发
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("接受事件：" + event);
    }
}
