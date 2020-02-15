package util;

import org.junit.Test;
import org.mockito.Mockito;
import pwd.allen.service.MyService;

/**
 * @author 门那粒沙
 * @create 2020-02-15 21:37
 **/
public class RestTemplateTest {

    @Test
    public void get() {
        MyService myService = Mockito.mock(MyService.class);
        myService.printThree("abc");
    }
}
