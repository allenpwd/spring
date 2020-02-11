package pwd.allen.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.entity.Person;
import pwd.allen.service.PersonService;

import java.util.Map;

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
     * @MatrixVariable 矩阵变量，每个变量用;分隔，多个值用,分隔；如/get/12;app=WeiBo;ids=1,2
     *
     * 注意：默认这个功能没开启，开启方式：
     *  1）xml方式：<mvc:annotation-driven enable-matrix-variables="true"/>
     *  2）注解配置方式：?.setRemoveSemicolonContent(false)，?可以是RequestMappingHandlerMapping
     *
     * @param id
     * @param matrixVars
     * @return
     */
    @MyAnnotation
    @RequestMapping("get/{id}")
    public Person getById(@PathVariable("id") Integer id, @MatrixVariable MultiValueMap matrixVars) {
        log.info("matrixVariable：{}", matrixVars);
        Person person = personService.getById(id);
        return person;
    }
}
