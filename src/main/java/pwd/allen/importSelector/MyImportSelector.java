package pwd.allen.importSelector;

import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 之定义逻辑返回需要导入的组件
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
        return new String[] {"pwd.allen.entity.Person"};
    }
}
