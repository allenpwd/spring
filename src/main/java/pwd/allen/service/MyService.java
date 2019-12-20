package pwd.allen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwd.allen.annotation.MyAnnotation;
import pwd.allen.annotation.MyQualifier;
import pwd.allen.entity.Fruit;

import java.util.List;

/**
 * @author pwd
 * @create 2018-11-11 12:00
 **/
public class MyService {

    @Autowired
    private Fruit fruit;

    public void printOne(String name) {
        System.out.println("hello!" + name);
        //throw new RuntimeException("出错了");
    }

    public void printTwo(String name) {
        System.out.println("hello!" + name);
    }

    @MyAnnotation
    public void printThree(String name) {
        System.out.println("hello!" + name);
    }
}
