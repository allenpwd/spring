package pwd.allen.factoryBean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import pwd.allen.entity.Order;
import pwd.allen.entity.Person;

/**
 * @author pwd
 * @create 2018-11-11 14:53
 **/
@Component
public class MyFactoryBean implements FactoryBean<Order> {
    public Order getObject() throws Exception {
        Order order = new Order();
        order.setName("潘伟丹");
        return order;
    }

    public Class<?> getObjectType() {
        return Order.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
