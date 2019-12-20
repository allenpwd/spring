package pwd.allen.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * 自定义一个注解，测试看看@Autowired会不会判断其他注解
 * @author 门那粒沙
 * @create 2019-05-28 14:14
 **/
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface MyQualifier {
}
