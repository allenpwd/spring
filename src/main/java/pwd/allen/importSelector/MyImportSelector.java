package pwd.allen.importSelector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 返回需要导入的组件
 *
 * 执行时机：在Beanfactory标准初始化之后，执行BeanFactory后置处理器的时候
 *  * ConfigurationClassPostProcessor（实现了BeanDefinitionRegistryPostProcessor）的postProcessBeanDefinitionRegistry方法
 *
 * @author pwd
 * @create 2018-11-11 14:16
 **/
public class MyImportSelector implements ImportSelector {

    /**
     *
     * @param importingClassMetadata 当前标注@Import注解的类的所有注解信息
     * @return 方法不能返回null，否则报错【NullPointerException】
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//        return new String[] {"pwd.allen.entity.Person", "pwd.allen.importBeanDefinitionRegistrar.MyImportBeanDefinitionRegistrar"};
        return new String[] {"pwd.allen.entity.Person"};
    }
}
