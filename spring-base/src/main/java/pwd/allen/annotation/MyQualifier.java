package pwd.allen.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * 自定义一个注解，测试看看@Autowired对Qualifier的判断
 *
 * 结论：如果本注解与@Autowired注解结合，则只会注入类型相同且qulifier为fruit的或者beanName为fruit的bean
 *
 * @author 门那粒沙
 * @create 2019-05-28 14:14
 **/
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("fruit")
public @interface MyQualifier {
}
