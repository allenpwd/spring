package pwd.allen.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import pwd.allen.entity.Fruit;

/**
 * @author pwd
 * @create 2018-11-03 19:43
 **/
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof Fruit) {
            System.out.println("调用BeanPostProcessor的postProcessBeforeInitialization方法，beanName:" + s);
        }
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o instanceof Fruit) {
            System.out.println("调用BeanPostProcessor的postProcessAfterInitialization方法，beanName:" + s);
        }
        return o;
    }
}
