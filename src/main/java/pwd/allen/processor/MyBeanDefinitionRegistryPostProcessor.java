package pwd.allen.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import pwd.allen.entity.Fruit;
import pwd.allen.util.CopyUtils;

import java.util.Arrays;

/**
 * @author pwd
 * @create 2018-12-01 16:36
 **/
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * BeanFactoryPostProcessor接口的方法
     * 执行时机：所有bean定义已经保存并加载到beanFactory、但bean实例还未创建
     *
     * 原理
     * 1）、ioc容器创建对象
     * 2）、refresh()->invokeBeanFactoryPostProcessors(beanFactory);
     *  如何找到所有的BeanFactoryPostProcessor并执行他们的方法：
     *      1）、直接在BeanFactory中找到所有类型是BeanFactoryPostProcessor的组件，并执行他们的方法
     *      2）、在初始化创建其他组件前面执行
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryPostProcessor.postProcessBeanFactory");
        int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        System.out.println("bean数量:" + beanDefinitionCount);
        System.out.println("beanDefinitionNames:" + Arrays.asList(beanDefinitionNames));
    }

    /**
     * BeanDefinitionRegistryPostProcessor接口在BeanFactoryPostProcessor基础上额外定义的方法
     *执行时机：所有bean定义信息将要被加载、bean实例未被创建（在postProcessBeanFactory之前）
     *
     *原理
     * 1）、ioc容器创建对象
     * 2）、refresh()->invokeBeanFactoryPostProcessors(beanFactory);
     * 3）、从容器中获取所有的BeanDefinitionRegistryPostProcessor组件，
     * 依次触发postProcessBeanDefinitionRegistry方法
     * 再来执行postProcessBeanFactory
     *
     * @param registry Bean定义信息的保存中心，以后BeanFactory就是按照这些信息创建Bean实例的
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry");
        System.out.println("bean数量：" + registry.getBeanDefinitionCount());

        /**注册beanDefinition begin **/
        //方法一
        BeanDefinition beanDefinition = new RootBeanDefinition(CopyUtils.class);
        //方法二
        //beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(CopyUtils.class).getBeanDefinition();
        registry.registerBeanDefinition("copyUtil", beanDefinition);
        /**注册beanDefinition end **/
    }
}
