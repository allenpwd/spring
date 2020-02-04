package pwd.allen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pwd.allen.entity.Person;
import pwd.allen.service.PersonService;

/**
 * @author 门那粒沙
 * @create 2020-02-04 21:18
 **/
@RequestMapping("person")
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("get")
    public Person get(@RequestParam("id") Integer id) {
        return personService.getById(id);
    }
}
