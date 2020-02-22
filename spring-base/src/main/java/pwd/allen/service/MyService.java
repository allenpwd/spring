package pwd.allen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.annotation.MyQualifier;
import pwd.allen.entity.Fruit;

import javax.annotation.Resource;
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

    /**
     * @Resource JSR-250注解，name属性指定要注入的bean的名称，若name没有指定，则默认为字段名称（标注在字段上）、或者方法传参的参数名（标注在setter方法上）
     *
     * @Resource 和 @Autowired的区别：
     *  注入List<Fruit>类型的属性时，@Resource会注入指定name的list类型的bean；@Autowired会把所有类型为Fruit的bean组成List返回
     *
     * @see org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
     */
    @Resource(name = "fruitFromAnnotation")
    private Fruit fruit;

    private StringValueResolver valueResolver;

    public void printOne(String name) {
//        System.out.println("hello!" + name);
        this.printThree(name);
//        ((MyService) AopContext.currentProxy()).printThree(name);//在被代理方法里调用代理对象的方法，不推荐，因为依赖了AOP代码
    }

    public void printTwo(String name) {
//        System.out.println("hello!" + name);
        this.printThree(name);
    }

    @MyAnnotation
    public void printThree(String name) {
        System.out.println("hello!" + name);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.valueResolver = resolver;
    }

    /**
     * EmbeddedValueResolver解析属性中的占位符和spel表达式
     * @param value
     * @return
     */
    public String resolveValue(String value) {
        return valueResolver.resolveStringValue(value);
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public Fruit getFruit() {
        return fruit;
    }
}
