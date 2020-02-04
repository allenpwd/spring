package util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import pwd.allen.config.MainConfig;
import pwd.allen.dao.PersonDao;
import pwd.allen.entity.Person;

/**
 * 编程式事务管理方式
 * 继承AbstractTransactionalJUnit4SpringContextTests之后，可以使用applicationContext和jdbcTemplate实例变量
 * 需要在ApplicationContext中定义一个 DataSource 和 PlatformTransactionManager 的bean
 *
 * @author lenovo
 * @create 2020-02-01 14:47
 **/
@ContextConfiguration(classes = MainConfig.class)
public class DaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PersonDao personDao;

    @Test
    public void insert() {

        //JdbcTestUtils提供了很多基于jdbcTemplate的测试方法
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "db_user");
        System.out.println("db_user.rows=" + rows);

        System.out.println(TestTransaction.isActive());

        Person person = new Person();
        person.setName("沙雕");
        person.setAge(40);
        int insert = personDao.insert(person);
        System.out.println(insert);

//        TestTransaction.flagForCommit();
        TestTransaction.flagForRollback();
        TestTransaction.end();
    }
}
