package pwd.allen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pwd.allen.entity.Fruit;

import javax.annotation.Resource;

/**
 * @author lenovo
 * @create 2020-01-23 17:49
 **/
@Service
public abstract class LookUpService {

    /**
     * spring会通过CGLIB重新生成子类，重新Lookup指定的方法，动态返回bean对象，若指定的bean不是单例，则每次调用返回的都是不同的
     *
     * xml配置方式：lookup-method
     *
     * 注意：由于使用cglib生成子类的方式，类不能是final，Lookup注释的方法也不能加final
     *
     * @return
     */
    @Lookup("prototypeFruit")
    public abstract Fruit getFruit();

    public String myMethod(String name) {
        return this.getClass().getName() + ":" + name;
    }
}
