package pwd.allen.entity;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 *
 * bean生命周期的几个回调方式的顺序：
 *      注解@PostConstruct标注的方法
 *      接口InitializingBean的实现方法afterPropertiesSet
 *      init-method属性指定的方法
 *      接口SmartInitializingSingleton的实现方法afterSingletonsInstantiated
 *
 *
 * @author pwd
 * @create 2018-08-11 21:05
 **/
public class Fruit implements InitializingBean, DisposableBean, SmartInitializingSingleton {

    @Value("苹果")
    private String name;
    private Date createAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", createAt=" + createAt +
                '}';
    }

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
}
