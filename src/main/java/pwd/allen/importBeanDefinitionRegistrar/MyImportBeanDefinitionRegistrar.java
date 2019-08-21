package pwd.allen.importBeanDefinitionRegistrar;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import pwd.allen.entity.Customer;
import pwd.allen.entity.Person;

/**
 * 执行时机：在Beanfactory标准初始化之后，执行BeanFactory后置处理器的时候
 * ConfigurationClassPostProcessor（实现了BeanDefinitionRegistryPostProcessor）的postProcessBeanDefinitionRegistry方法
 *
 * @author pwd
 * @create 2018-11-11 14:31
 **/
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     *
     * @param importingClassMetadata 当前类的注解信息
     * @param registry  BeanDefinition注册类，通过registerBeanDefinition方法注册
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean fruit = registry.containsBeanDefinition("fruit");
        if (fruit) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Customer.class);
            registry.registerBeanDefinition("myCustomer", beanDefinition);
        }
    }
}
