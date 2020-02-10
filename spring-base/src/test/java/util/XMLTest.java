package util;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.castor.CastorMarshaller;
import pwd.allen.entity.Customer;
import pwd.allen.entity.Order;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author 门那粒沙
 * @create 2020-02-10 15:39
 **/
public class XMLTest {

    private Customer customer;

    @Before
    public void setup() {
        List<Order> list = Arrays.asList(
                new Order("订单1", 12.5f)
                , new Order("订单2", 13f)
        );
        customer = new Customer("测试xml序列化", new Date(), list);
    }

    /**
     * 测试使用CastorMarshaller进行xml序列化
     * @throws IOException
     */
    @Test
    public void castorMarshaller() throws IOException {
        //需要放在spring容器中经后置处理器处理以便初始化
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(CastorMarshaller.class);
        CastorMarshaller castorMarshaller = applicationContext.getBean(CastorMarshaller.class);

        //序列化
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        castorMarshaller.marshal(customer, new StreamResult(outputStream));
        String xml = new String(outputStream.toByteArray());
        System.out.println(xml);

        //反序列化
        Customer customerTwo = (Customer) castorMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(outputStream.toByteArray())));
        System.out.println(customerTwo);
    }

    /**
     * 使用JAXB进行xml序列化
     * @throws JAXBException
     */
    @Test
    public void jaxb() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Customer.class);

        //序列化
        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(customer, new StreamResult(outputStream));
        String xml = new String(outputStream.toByteArray());
        System.out.println(xml);

        //反序列化
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Customer customerTwo = (Customer) unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(outputStream.toByteArray())));
        System.out.println(customerTwo);
    }
}
