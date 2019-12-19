package pwd.allen.importBeanDefinitionRegistrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import pwd.allen.entity.Customer;

/**
 *
 * 作用：根据@Configuration注释的配置类来注册额外的BeanDefinition
 * 不确定：所以这个注册的功能要生效的话要要在配置类里以Bean定义级别的方式注入，比如@Import或者ImportSelector的形式注入，通过@Bean的方式是不会生效的
 *
 * 执行时机：
 * 在Beanfactory标准初始化之后，执行BeanFactory后置处理器的时候（invokeBeanFactoryPostProcessors）
 * 会去调用所有的BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法
 * 其中有一个ConfigurationClassPostProcessor，
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
