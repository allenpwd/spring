package pwd.allen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pwd.allen.config.MainConfig;
import pwd.allen.entity.Fruit;
import pwd.allen.entity.Person;
import pwd.allen.service.LookUpService;
import pwd.allen.service.MyService;

/**
 * @author pwd
 * @create 2018-11-03 14:55
 **/
public class AnnotationTest {
    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init() {
        //任意@Companent或JSR-330注解的类都可以作为构造方法的输入
        applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
    }

    @After
    public void destroy() {
        applicationContext.close();
    }

    @Test
    public void testOne() {
        //如果有多个Fruit实例，获取标注了primary=true的那个
        Fruit fruit = (Fruit) applicationContext.getBean(Fruit.class);
        Person person = applicationContext.getBean(Person.class);
        System.out.println(person);

        //region 测试AOP
        //这里拿到的bean是代理对象，看到的fruits属性可能是null，但是不代表被代理对象没有fruits
        MyService myService = applicationContext.getBean(MyService.class);
//        myService.print("潘伟丹");
        myService.printTwo("潘伟丹");
        //endregion

        /** test transaction aop begin **/
//        PersonService personService = applicationContext.getBean(PersonService.class);
//        System.out.println("insert:" + personService.insertUser());
        /** test transaction aop end **/

    }

    /**
     * 测试@Lookup动态改变bean实现，每次调用都重新从bean容器中获取name为fruit的bean，若是prototype类型则每次都创建新的
     */
    @Test
    public void testLookup() {
        LookUpService bean = applicationContext.getBean(LookUpService.class);
        Fruit fruit = bean.getFruit();
        System.out.println(fruit);
        System.out.println(bean.myMethod("测试replaced-method"));
    }

    @Test
    public void other() {
        String str = "#{'os.name=${os.name}'} ${jdbc.url} #{12+35}";

        //使用beanFactory解析属性中的占位符
        System.out.println(applicationContext.getBeanFactory().resolveEmbeddedValue(str));

        //EmbeddedValueResolver解析属性中的占位符和spel表达式，先使用beanFactory解析占位符，再使用BeanExpressionResolver解析spel表达式
        System.out.println(applicationContext.getBean(MyService.class).resolveValue(str));
    }

    @Test
    public void printAllBean() {
        System.out.println("------------------printAllBean begin------------------");
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
        System.out.println("------------------printAllBean end------------------");
    }
}
