package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import pwd.allen.entity.Fruit;
import pwd.allen.entity.Order;

import java.util.Date;

/**
 * @author 门那粒沙
 * @create 2020-02-11 22:22
 **/
public class OtherTest {

    @Test
    public void test() throws JsonProcessingException {
        Order order = new Order("香蕉", 12.5f);

        ObjectMapper objectMapper = new ObjectMapper();
        // 转换为格式化的json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String str = objectMapper.writeValueAsString(order);
        System.out.println(str);
        Order order1 = objectMapper.readValue(str, Order.class);
        System.out.println(order1);

    }
}
