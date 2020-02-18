package pwd.allen.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.entity.Person;
import pwd.allen.service.PersonService;

/**
 * @author 门那粒沙
 * @create 2020-02-04 21:18
 **/
@Log4j2
@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * @param id
     * @return
     */
    @MyAnnotation
    @RequestMapping("get/{id}")
    public Person getById(@PathVariable("id") Integer id) {
        Person person = personService.getById(id);
        return person;
    }
}
