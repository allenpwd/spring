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
 * 不确定：所以这个注册的功能要生效的话要要在配置类里以BeanDefinition级别的方式注入（如@Import或者ImportSelector），通过@Bean或者xml<bean>的方式是不会生效的
 *
 * 执行时机：
 * 在Beanfactory标准初始化之后，执行BeanFactory后置处理器的时候（invokeBeanFactoryPostProcessors）
 * 会去调用所有的BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法
 * 其中有一个叫ConfigurationClassPostProcessor的会去检索配置类中引入的Bean定义级别的ImportBeanDefinitionRegistrar，然后遍历调用registerBeanDefinitions
 *
 * @author pwd
 * @create 2018-11-11 14:31
 **/
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     *  如果有fruit的beanDifinition则注册customer这个beanDifinition
     *
     * @param importingClassMetadata 导入当前类的配置类的注解元数据
     * @param registry  BeanDefinition注册类，通过registerBeanDefinition方法注册
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println("配置类：" + importingClassMetadata.getClassName());
        boolean fruit = registry.containsBeanDefinition("fruit");
        if (fruit) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Customer.class);
            registry.registerBeanDefinition("customer", beanDefinition);
        }
    }
}
