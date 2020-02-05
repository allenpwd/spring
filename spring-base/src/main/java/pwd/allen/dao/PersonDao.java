package pwd.allen.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pwd.allen.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public Person getById(Integer id) {
        String sql = "select * from db_user where id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, new ResultSetExtractor<Person>() {
            @Override
            public Person extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Person person = null;

                if (resultSet.next()) {
                    person = new Person();
                    person.setId(resultSet.getInt("id"));
                    person.setAge(resultSet.getInt("age"));
                    person.setName(resultSet.getString("user_name"));
                }
                return person;
            }
        });
    }
}
