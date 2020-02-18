package pwd.allen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author pwd
 * @create 2018-11-11 14:56
 **/
@Profile("default")
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "pay")
    private float pay;
}
