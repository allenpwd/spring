package pwd.allen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.dao.PersonDao;
import pwd.allen.entity.Person;

import java.util.UUID;

/**
 * @author pwd
 * @create 2018-11-28 22:57
 **/
@Service
public class PersonService {

    @Autowired
    PersonDao personDao;

    @Transactional
    @MyAnnotation
    public int insertUser() {
        int result = 0;
        Person person = new Person();
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        person.setName(uuid);
        person.setAge(16);
        result = personDao.insert(person);
        return result;
    }

    public Person getById(Integer id) {
        return personDao.getById(id);
    }
}
