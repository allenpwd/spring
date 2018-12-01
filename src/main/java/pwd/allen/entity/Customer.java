package pwd.allen.entity;

import java.util.Date;

/**
 * @author pwd
 * @create 2018-11-11 14:43
 **/
public class Customer {
    private String name;
    private Date createAt;

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
