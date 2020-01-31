package pwd.allen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pwd.allen.aop.MyAspect;
import pwd.allen.config.MainConfig;
import pwd.allen.entity.Fruit;
import pwd.allen.entity.Person;
import pwd.allen.service.LookUpService;
import pwd.allen.service.MyService;

import java.util.Locale;

/**
 * @author pwd
 * @create 2018-11-03 14:55
 **/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MainConfig.class)
@TestPropertySource(properties = {"test.one=fucking", "test.two: 4040"})
public class AnnotationTest {

    /**
     * 任意@Companent或JSR-330注解的类都可以作为AnnotationConfigApplicationContext构造方法的输入
     */
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    public void testOne() {
        //如果有多个Fruit实例，获取标注了primary=true的那个
        Fruit fruit = (Fruit) applicationContext.getBean(Fruit.class);
        Person person = applicationContext.getBean(Person.class);
        System.out.println(person);

        /** test transaction aop begin **/
//        PersonService personService = applicationContext.getBean(PersonService.class);
//        System.out.println("insert:" + personService.insertUser());
        /** test transaction aop end **/

    }

    @Test
    public void aop() {
        //这里拿到的bean是代理对象，看到的fruits属性可能是null，但是不代表被代理对象没有fruits
        MyService myService = applicationContext.getBean(MyService.class);
        myService.printOne("allen");
//        myService.printTwo("allen");

        //模拟aop TODO 有问题：JoinPoint.getTarget()返回null
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(new MyService());
        proxyFactory.addAspect(MyAspect.class);//添加切面类，该类必须有@Aspectj标注
        proxyFactory.setExposeProxy(true);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.setTargetClass(MyService.class);
        MyService myService1 = proxyFactory.getProxy();
//        myService1.printOne("手动");
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
    public void resolveValue() {
        String str = "#{'os.name=${os.name}'}\njdbc.url=${jdbc.url}\n算术:#{12+35}\n#{'fruit.name='+fruit.name}\n${test.one} ${test.two}";

        //使用beanFactory解析属性中的占位符
        System.out.println(applicationContext.getBeanFactory().resolveEmbeddedValue(str));

        //EmbeddedValueResolver解析属性中的占位符和spel表达式，先使用beanFactory解析占位符，再使用BeanExpressionResolver解析spel表达式
        System.out.println(applicationContext.getBean(MyService.class).resolveValue(str));
    }

    @Test
    public void messageSource() {
        MessageSource messageSource = applicationContext;
        String code = "pwd.allen.desc";
        Object[] args = new Object[]{"胡闹", 30};
        System.out.println(messageSource.getMessage(code, args, Locale.US));
        System.out.println(messageSource.getMessage(code, args, Locale.SIMPLIFIED_CHINESE));
    }

    @Repeat(2)
    @Test
    public void printAllBean() {
        System.out.println("------------------printAllBean begin------------------");
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
        System.out.println("------------------printAllBean end------------------");
    }
}
