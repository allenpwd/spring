package pwd.allen.annotation;

import java.lang.annotation.*;

/**
 * 用于asject 注解标识的方法作为AOP切入点
 * @author 门那粒沙
 * @create 2019-05-28 14:14
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnotation {
}
