package pwd.allen.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import pwd.allen.config.MainConfig;
import pwd.allen.dao.PersonDao;
import pwd.allen.entity.Person;

/**
 * 编程式事务管理方式
 *
 * @author lenovo
 * @create 2020-02-01 14:47
 **/
@ContextConfiguration(classes = MainConfig.class)
public class DaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PersonDao personDao;

    @Test
    @Transactional
    public void insert() {

        Person person = new Person();
        person.setName("沙雕");
        person.setAge(40);

        System.out.println(TestTransaction.isActive());

        int insert = personDao.insert(person);
        System.out.println(insert);

//        TestTransaction.flagForRollback();
        TestTransaction.end();
    }
}
