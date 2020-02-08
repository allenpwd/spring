package pwd.allen.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pwd.allen.entity.Person;
import pwd.allen.service.PersonService;

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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public int insert(Person person) {
        //发布一个事件，用于测试@TransactionalEventListener
        eventPublisher.publishEvent(new MyTransactionEvent("我是个与事务相关的事件"));

        String sql = "INSERT INTO db_user(user_name,age) values (?, ?)";
        return jdbcTemplate.update(sql, person.getName(), person.getAge());
    }

    public Person getById(Integer id) {

        RowMapper<Person> rowMapper = new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = null;
                person = new Person();
                person.setId(rs.getInt("id"));
                person.setAge(rs.getInt("age"));
                person.setName(rs.getString("user_name"));
                return person;
            }
        };

        //方法一
//        String sql = "select * from db_user where id=?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);

        //另一种方法：封装成NamedParameterJdbcTemplate，可以使用命名参数符号，还可以包装javaBean来作为参数来源
        String sql = "select * from db_user where id=:id";
        Person person = new Person();
        person.setId(id);
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(sql, new BeanPropertySqlParameterSource(person), rowMapper);
    }

    /**
     * 自定义一个事务事件
     */
    private static class MyTransactionEvent extends ApplicationEvent {

        public MyTransactionEvent(Object source) {
            super(source);
        }

    }
}
