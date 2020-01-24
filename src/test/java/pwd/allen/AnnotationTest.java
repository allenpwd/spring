package pwd.allen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pwd.allen.config.MainConfig;
import pwd.allen.entity.Customer;
import pwd.allen.entity.Fruit;
import pwd.allen.entity.Person;
import pwd.allen.service.LookUpService;
import pwd.allen.service.MyService;

import java.time.Period;

/**
 * @author pwd
 * @create 2018-11-03 14:55
 **/
public class AnnotationTest {
    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
    }

    @After
    public void destroy() {
        applicationContext.close();
    }

    @Test
    public void testOne() {
        Fruit fruit = (Fruit) applicationContext.getBean("fruit");
        Person person = applicationContext.getBean(Person.class);
        System.out.println(person);
        LifecycleProcessor lifecycleProcessor = applicationContext.getBean(LifecycleProcessor.class);
        System.out.println(lifecycleProcessor);

        //region 测试AOP
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
     * 测试@Lookup动态改变bean实现
     */
    @Test
    public void testLookup() {
        LookUpService bean = applicationContext.getBean(LookUpService.class);
        Fruit fruit = bean.getFruit();
        System.out.println(fruit);
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
