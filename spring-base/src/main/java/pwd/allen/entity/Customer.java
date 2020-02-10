package pwd.allen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * @author pwd
 * @create 2018-11-11 14:43
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement(name = "cust")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "create_at")
    private Date createAt;

    @XmlElement(name = "order")
    private List<Order> orders;
}
