package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import pwd.allen.entity.Customer;
import pwd.allen.entity.Order;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author 门那粒沙
 * @create 2020-02-11 22:22
 **/
public class OtherTest {

    /**
     * 测试jackson-databind的ObjectMapper工具
     * Include属性
     *  Include.ALWAYS  是序列化对像所有属性
     *  Include.NON_NULL 只有不为null的字段才被序列化
     *  Include.NON_EMPTY 如果为null或者 空字符串和空集合都不会被序列化
     *
     * @throws JsonProcessingException
     */
    @Test
    public void objectMapper() throws JsonProcessingException {
        Order order = new Order("香蕉", 12.5f);
        Customer customer = new Customer("顾客", new Date(), Arrays.asList(order));

        ObjectMapper objectMapper = new ObjectMapper();
        //不处理null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候（对应的属性没有get方法）,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        String str = objectMapper.writeValueAsString(customer);
        System.out.println(str);
        Customer customer1 = objectMapper.readValue(str, Customer.class);
        System.out.println(customer1);

    }
}
