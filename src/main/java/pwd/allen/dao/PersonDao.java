package pwd.allen.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pwd.allen.entity.Person;

/**
 * @author pwd
 * @create 2018-11-28 22:51
 **/
@Repository
public class PersonDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insert(Person person) {
        String sql = "INSERT INTO db_user(user_name,age) values (?, ?)";
        return jdbcTemplate.update(sql, person.getName(), person.getAge());
    }
}
