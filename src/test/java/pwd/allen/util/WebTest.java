package pwd.allen.util;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用 {@link MockHttpServletRequest}和 {@link MockHttpSession} 测试 request和session的bean
 * 需要标注@WebAppConfiguration
 *
 * @author lenovo
 * @create 2020-02-01 9:43
 **/
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class WebTest {

    @Configuration
    static class TestConfig {
        @Data
        static class TestEntity {
            @Value("#{request.getParameter('name')}")
            private String name;
            @Value("#{request.getAttribute('age')}")
            private Integer age;
        }

        //region request作用域的bean，可以使用spel表达式读取request
//        @Bean
//        @Scope(WebApplicationContext.SCOPE_REQUEST)
//        public TestEntity fruitTest(HttpServletRequest request) {
//            TestEntity entity = new TestEntity();
//            entity.setName(request.getParameter("name"));
//            return entity;
//        }
        @Bean
        @Scope(WebApplicationContext.SCOPE_REQUEST)
        public TestEntity testEntity() {
            return new TestEntity();
        }
        //endregion


    }

    @Autowired
    private MockHttpServletRequest request;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test() {
        request.setParameter("name", "this is a test!");
        request.setAttribute("age", "23");

        TestConfig.TestEntity testEntity = applicationContext.getBean(TestConfig.TestEntity.class);
        System.out.println(testEntity);
    }
}
