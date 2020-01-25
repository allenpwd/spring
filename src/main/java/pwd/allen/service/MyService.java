package pwd.allen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.annotation.MyQualifier;
import pwd.allen.entity.Fruit;

import java.util.List;

/**
 * @author pwd
 * @create 2018-11-11 12:00
 **/
public class MyService implements EmbeddedValueResolverAware {

    /**
     * 这里加了@MyQualifier注解，指定了qualifier的value为fruit
     * 效果是只会注入beanName为fruit或者同样有指定qualifier的value为fruit的bean
     *
     */
    @MyQualifier
    @Autowired
    private List<Fruit> fruits;

    public void printOne(String name) {
        System.out.println("hello!" + name);
        //throw new RuntimeException("出错了");
    }

    public void printTwo(String name) {
        System.out.println("hello!" + name);
    }

    @MyAnnotation
    public void printThree(String name) {
        System.out.println("hello!" + name);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        System.out.println(resolver.resolveStringValue("${os.name}"));
    }
}
