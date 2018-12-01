package pwd.allen.importBeanDefinitionRegistrar;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import pwd.allen.entity.Customer;
import pwd.allen.entity.Person;

/**
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
