package pwd.allen.batch;

import org.springframework.batch.item.ItemProcessor;
import pwd.allen.entity.Person;

/**
 * ItemProcessor用于处理业务逻辑、校验、过滤、转换等
 *
 * @author 门那粒沙
 * @create 2020-02-23 14:28
 **/
public class MyItemProcessor implements ItemProcessor<Person, Person> {

    /**
     *  如果返回null，则表示该item被过滤，即不会被itemWriter处理
     * @param person
     * @return
     * @throws Exception
     */
    @Override
    public Person process(Person person) throws Exception {
        person.setUserName(person.getUserName().toUpperCase());
        return person;
    }
}
