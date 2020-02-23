package pwd.allen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * JAXB（Java Architecture for XML Binding) 实现XML与Bean的相互转换
 *  @XmlAccessorType(XmlAccessType.FIELD) 控制字段或属性的序列化。FIELD表示JAXB将自动绑定Java类中的每个非静态的（static）、非瞬态的（由@XmlTransient标 注）字段到XML。其他值还有XmlAccessType.PROPERTY和XmlAccessType.NONE
 *  @XmlAttribute 将属性映射到XML属性
 *  @XmlElement 将属性映射到XML元素
 *  @XmlAccessorOrder 控制JAXB绑定类中属性和字段的排序
 *  @XmlElementWrapper 主要用于生成一个包装集合的包装器 XML 元素
 *
 * @author pwd
 * @create 2018-11-11 14:43
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement(name = "cust")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {
//    @XmlAttribute(name = "name")//不加的话会默认映射为xml元素
    private String name;

    @XmlAttribute(name = "create_at")
    private Date createAt;

    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "order")
    private List<Order> orders;
}
