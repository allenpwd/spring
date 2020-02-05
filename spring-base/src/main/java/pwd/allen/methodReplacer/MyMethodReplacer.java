package pwd.allen.methodReplacer;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 可以通过replaced-method注入来动态地将bean方法替换成 {@link MethodReplacer} 接口实现
 * 原理：使用CGLIB，重新生成子类，重写配置方法和对象
 *
 * @author lenovo
 * @create 2020-01-23 18:58
 **/
@Deprecated
public class MyMethodReplacer implements MethodReplacer {

    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        //不能调用method，否则死循环
//        Object rel = method.invoke(obj, args);
        String rel = this.getClass().getName() + ":" + Arrays.toString(args);
        return 12;
    }
}
