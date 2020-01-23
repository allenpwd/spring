package pwd.allen.entity;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author pwd
 * @create 2018-11-11 14:56
 **/
@Profile("default")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class Order {
    private String name;
    private float pay;
}
