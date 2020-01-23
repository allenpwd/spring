package pwd.allen.methodReplacer;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * 可以通过replace-method注入来动态地将bean方法替换成 {@link MethodReplacer} 接口实现
 * 原理：使用CGLIB，重新生成子类，重写配置方法和对象
 *
 * @author lenovo
 * @create 2020-01-23 18:58
 **/
public class MyMethodReplacer implements MethodReplacer {

    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        System.out.println(this.getClass().getName() + "[before]");
        Object rel = method.invoke(obj, args);
        System.out.println(this.getClass().getName() + "[after]");
        return rel;
    }
}
