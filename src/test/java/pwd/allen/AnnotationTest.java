package pwd.allen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pwd.allen.config.MainConfig;
import pwd.allen.service.MyService;

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
    public void test() {
//        Fruit fruit = (Fruit) applicationContext.getBean("fruit");
//        Person person = applicationContext.getBean(Person.class);
//        System.out.println(person);

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


    @Test
    public void printAllBean() {
        System.out.println("------------------printAllBean begin------------------");
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
        System.out.println("------------------printAllBean end------------------");
    }
}
