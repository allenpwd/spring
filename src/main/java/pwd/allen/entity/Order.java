package pwd.allen.entity;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author pwd
 * @create 2018-11-11 14:56
 **/
@Profile("default")
@Component
public class Order {
    private String name;
    private float pay;


    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", pay=" + pay +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }
}
