package pwd.allen.entity;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @author pwd
 * @create 2018-08-11 21:05
 **/
public class Fruit implements InitializingBean, DisposableBean {

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
}
