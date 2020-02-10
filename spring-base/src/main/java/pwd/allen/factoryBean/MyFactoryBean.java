package pwd.allen.factoryBean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pwd.allen.entity.Order;

/**
 * bean工厂方式创建bean
 * @author pwd
 * @create 2018-11-11 14:53
 **/
@Primary
@Component
public class MyFactoryBean implements FactoryBean<Order> {
    public Order getObject() throws Exception {
        Order order = new Order();
        order.setName("pwd");
        return order;
    }

    public Class<?> getObjectType() {
        return Order.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
