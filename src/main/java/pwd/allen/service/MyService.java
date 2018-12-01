package pwd.allen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwd.allen.entity.Fruit;

/**
 * @author pwd
 * @create 2018-11-11 12:00
 **/
public class MyService {
    @Autowired
    private Fruit fruit;

    public void print(String name) {
        System.out.println("hello!" + name);
        //throw new RuntimeException("出错了");
    }
}
