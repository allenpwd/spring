package util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import pwd.allen.config.MainConfig;
import pwd.allen.dao.PersonDao;
import pwd.allen.entity.Person;

import java.sql.SQLException;

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

    /**
     * 手动控制事务回滚
     */
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

        //TODO 不知道怎么用
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    /**
     * 测试抛出异常时是否回滚
     * 结果：在没指定回滚异常/不回滚异常的情况下，
     *  已检查的异常（除RuntimeException之外的Exception子类，包括Exception）不会回滚
     *  未检查异常（RuntimeException及子类、Error）默认会回滚
     *
     * @throws Exception
     */
    @Test
    @Transactional
//    @Transactional(rollbackFor = Throwable.class, noRollbackFor = SQLException.class) //除了SQLException其他任何异常都回滚
    @Commit//不加这个Spring TestContext会默认回滚；也可以替换成@Rollback(false)
    public void insertTwo() throws Exception {
        Person person = new Person();
        person.setName("奥利给");
        person.setAge(40);
        int insert = personDao.insert(person);
        System.out.println(insert);

        //不会导致回滚
        throw new SQLException("自定义的已检查异常");
    }

    @Test
    public void get() {
        Person person = personDao.getById(1);
        System.out.println(person);
    }

}
