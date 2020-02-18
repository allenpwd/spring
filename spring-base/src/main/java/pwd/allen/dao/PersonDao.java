package pwd.allen.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pwd.allen.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author pwd
 * @create 2018-11-28 22:51
 **/
@Repository
public class PersonDao {

    JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert addPerson;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.addPerson = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("db_user")
                .usingGeneratedKeyColumns("id");//指定需要数据库自动生成主键的列名
    }

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public int insert(Person person) {
        //发布一个事件，用于测试@TransactionalEventListener
        eventPublisher.publishEvent(new MyTransactionEvent("我是个与事务相关的事件"));

        String sql = "INSERT INTO db_user(user_name,age) values (?, ?)";
        return jdbcTemplate.update(sql, person.getUserName(), person.getAge());
    }

    /**
     * 使用SimpleJdbcInsert来插入数据，并返回自动生成的id
     * 介绍：SimpleJdbcInsert利用JDBC驱动所提供的数据库元数据的特性来简化操作配置
     *
     * @param person
     * @return
     */
    public Person add(Person person) {
        SqlParameterSource parameterSource = null;

        //方式一：bean，字段要相对应，否则传参是null
        parameterSource = new BeanPropertySqlParameterSource(person);

        //方式二：map
//        parameterSource = new MapSqlParameterSource("user_name", person.getUserName())
//                .addValue("age", person.getAge());

        Number key = addPerson.executeAndReturnKey(parameterSource);
        person.setId(key.intValue());
        return person;
    }

    public Person getById(Integer id) {

        RowMapper<Person> rowMapper = new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = null;
                person = new Person();
                person.setId(rs.getInt("id"));
                person.setAge(rs.getInt("age"));
                person.setUserName(rs.getString("user_name"));
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
