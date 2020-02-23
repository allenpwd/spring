package pwd.allen.batch;

import org.springframework.batch.item.ItemProcessor;
import pwd.allen.entity.Person;

/**
 * @author 门那粒沙
 * @create 2020-02-23 14:28
 **/
public class MyItemProcessor implements ItemProcessor<Person, Person> {
    @Override
    public Person process(Person person) throws Exception {
        person.setUserName(person.getUserName().toUpperCase());
        return person;
    }
}
