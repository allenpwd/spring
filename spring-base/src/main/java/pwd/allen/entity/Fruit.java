package pwd.allen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 *
 * bean生命周期的几个回调方式的顺序：
 *   初始化（在该bean创建并且所有必要依赖配置完成后）：
 *      注解@PostConstruct标注的方法【applyBeanPostProcessorsBeforeInitialization-》CommonAnnotationBeanPostProcessor.postProcessBeforeInitialization】
 *      接口InitializingBean的实现方法afterPropertiesSet（不推荐，因为代码与接口耦合）【invokeInitMethods】
 *      init-method属性指定的方法【invokeInitMethods】
 *      接口SmartInitializingSingleton的实现方法afterSingletonsInstantiated（不推荐，因为代码与接口耦合），单例情况下【preInstantiateSingletons】
 *
 *      SmartLifeCycle.start(所有bean准备好之后)【finishRefresh-》 DefaultLifecycleProcessor#startBeans 】
 *   销毁：
 *      注解@PreDestroy标注的方法
 *      接口DisposableBean的实现方法destroy（不推荐，因为代码与接口耦合）
 *
 *
 * @author pwd
 * @create 2018-08-11 21:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fruit implements InitializingBean, DisposableBean, SmartInitializingSingleton, SmartLifecycle {

    private String name;
    @Value("12")
    private Float price;
    private Date createAt;

    public void init() {
        System.out.println("@Bean init-method属性指定初始化方法");
    }

    public void say(String name) {
        System.out.println("Hi " + name + "!I am fruit!");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("调用@PostConstruct注解标注的方法");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("调用@PreDestroy注解标注的方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("调用接口DisposableBean实现方法destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("调用接口InitializingBean实现方法afterPropertiesSet");
    }

    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("调用接口SmartInitializingSingleton实现方法afterSingletonsInstantiated");
    }

    @Override
    public void start() {
        System.out.println("LiftCycle.start()");
    }

    @Override
    public void stop() {
        System.out.println("LiftCycle.stop()");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
