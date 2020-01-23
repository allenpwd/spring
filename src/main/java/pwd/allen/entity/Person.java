package pwd.allen.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author pwd
 * @create 2018-11-11 12:18
 **/
@Data
public class Person {
    @Value("${person.name}")
    private String name;
    @Value("#{20+6} ")
    private int age;

    @Qualifier("fruit")
    @Autowired
    private Fruit fruit;

    @Autowired
    private Order order;
}
