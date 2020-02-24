package util;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import pwd.allen.entity.Customer;
import pwd.allen.entity.Order;
import pwd.allen.entity.Person;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

    @Configuration
    static class TestConfig {

        @Bean
        public Jaxb2Marshaller jaxb2Marshaller() {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(Customer.class);
            return marshaller;
        }

        @Bean CastorMarshaller castorMarshaller() {
            return new CastorMarshaller();
        }
    }

    /**
     * 测试使用CastorMarshaller进行xml序列化
     * @throws IOException
     */
    @Test
    public void castorMarshaller() throws IOException {
        //需要放在spring容器中经后置处理器处理以便初始化
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
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
     * 测试使用jaxb2Marshaller进行xml序列化，实体类需要注解
     * @throws IOException
     */
    @Test
    public void jaxb2Marshaller() throws IOException {
        //需要放在spring容器中经后置处理器处理以便初始化
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        Jaxb2Marshaller castorMarshaller = applicationContext.getBean(Jaxb2Marshaller.class);

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
     * 使用jdk的JAXB进行xml序列化，需要在实体类上添加注解
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

    /**
     * 使用XStreamMarshaller
     * @throws JAXBException
     */
    @Test
    public void xstream() throws JAXBException, IOException {
        //设置xml反序列化工具
        XStreamMarshaller marshaller = new XStreamMarshaller();
        //customer元素映射成Customer类
        marshaller.setAliases(Collections.singletonMap("customer", Customer.class));

        //序列化
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(customer, new StreamResult(outputStream));
        String xml = new String(outputStream.toByteArray());
        System.out.println(xml);

        //反序列化
        Customer customerTwo = (Customer) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(outputStream.toByteArray())));
        System.out.println(customerTwo);
    }

    /**
     * 测试使用CastorMarshaller进行xml序列化
     * @throws IOException
     */
    @Test
    public void castorMarshaller2() throws IOException {
        Person person = new Person(12, "你好", 23);
        List<Person> list = Arrays.asList(person);

        CastorMarshaller castorMarshaller = new CastorMarshaller();
        castorMarshaller.setTargetClass(Person.class);
        castorMarshaller.afterPropertiesSet();

        //序列化
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        castorMarshaller.marshal(list, new StreamResult(outputStream));
        String xml = new String(outputStream.toByteArray());
        System.out.println(xml);

        //反序列化
        list = (List<Person>) castorMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(outputStream.toByteArray())));
        System.out.println(list);
    }
}
