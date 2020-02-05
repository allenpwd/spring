package pwd.allen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.entity.Person;
import pwd.allen.service.PersonService;

/**
 * @author 门那粒沙
 * @create 2020-02-04 21:18
 **/
@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @MyAnnotation
    @RequestMapping("get/{id}")
    public Person getById(@PathVariable("id") Integer id) {
        Person person = personService.getById(id);
        return person;
    }
}
