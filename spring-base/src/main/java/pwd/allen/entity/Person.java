package pwd.allen.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pwd
 * @create 2018-11-11 12:18
 **/
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Person implements Serializable {
    @NonNull
    private Integer id;

    @NonNull
    @Value("${person.name}")
    private String userName;

    @NonNull
    @Value("#{20+6} ")
    private Integer age;

    @Qualifier("fruit")
    @Autowired
    private Fruit fruit;

    private Date createAt;
}
