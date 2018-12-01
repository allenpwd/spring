package pwd.allen.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author pwd
 * @create 2018-11-11 12:18
 **/
public class Person {
    @Value("${person.name}")
    private String name;
    @Value("#{20+6} ")
    private int age;

    @Qualifier("fruit")
    @Autowired
    private Fruit fruit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", fruit=" + fruit +
                '}';
    }
}
