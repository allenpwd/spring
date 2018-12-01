package pwd.allen;

import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pwd.allen.annotation.MainConfig;
import pwd.allen.entity.Fruit;
import pwd.allen.entity.Person;
import pwd.allen.service.MyService;
import pwd.allen.service.PersonService;

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
    public void destory() {
        applicationContext.close();
    }

    @Test
    public void test() {
        printAllBean(applicationContext);
//        Fruit fruit = (Fruit) applicationContext.getBean("fruit");
//        Person person = applicationContext.getBean(Person.class);
//        System.out.println(person);

        /** AOP test begin **/
//        MyService myService = applicationContext.getBean(MyService.class);
//        myService.print("潘伟丹");
        /** AOP test end **/

        /** test transaction aop begin **/
//        PersonService personService = applicationContext.getBean(PersonService.class);
//        System.out.println("insert:" + personService.insertUser());
        /** test transaction aop end **/

    }


    public static void printAllBean(ApplicationContext applicationContext) {
        System.out.println("------------------printAllBean begin------------------");
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
        System.out.println("------------------printAllBean end------------------");
    }
}
