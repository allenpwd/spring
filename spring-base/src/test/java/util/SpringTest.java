package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.GenericGroovyXmlContextLoader;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.GenericGroovyXmlWebContextLoader;
import org.springframework.test.context.web.GenericXmlWebContextLoader;
import pwd.allen.entity.Fruit;

/**
 * Spring Test Context
 * 加载上下文的方式如下，不能同时使用，否则报错
 * 1）若@ContextConfiguration忽略了locations和value属性,则 Test Context框架将尝试检测默认的XML资源位置。
 *  具体来说, {@link GenericXmlContextLoader} 和 {@link GenericXmlWebContextLoader} 将根据测试类的名称检测默认位置。
 *  默认上下文xml为：包路径/测试类名_context.xml
 * 2）同上，{@link GenericGroovyXmlContextLoader} 和 {@link GenericGroovyXmlWebContextLoader} 将根据测试类的名称检测默认位置。
 *  默认配置文件为：包路径/测试类名.groovy
 * 3）若没有指定ContextConfiguration的classes，{@link AnnotationConfigContextLoader} 和 {@link AnnotationConfigWebContextLoader}
 *  将检测符合配置类实现要求的测试类的所有静态嵌套类
 *
 * @RunWith(SpringRunner.class)：获取Test Context框架的相关支持，如加载应用程序上下文、测试实例的依赖注入、事务测试方法执行等
 *
 * @author lenovo
 * @create 2020-01-23 10:56
 **/
@RunWith(SpringRunner.class)
public class SpringTest {

    /**
     * 上下文
     * （2）若没有指定ContextConfiguration的classes，{@link AnnotationConfigContextLoader} 和 {@link AnnotationConfigWebContextLoader}
     * 将检测符合配置类实现要求的测试类的所有静态嵌套类
     */
//    @Configuration
//    static class MyTest {
//        @Bean
//        public Fruit fruitTest() {
//            Fruit fruit = new Fruit();
//            fruit.setName("this is a test");
//            return fruit;
//        }
//    }

    @Autowired
    private Fruit fruit;

    @Repeat(2)//重复执行，要结合SpringJUnit4ClassRunner才行
    @Test
    public void test() {
        System.out.println(fruit);
    }

}
