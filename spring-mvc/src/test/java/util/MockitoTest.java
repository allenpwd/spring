package util;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import pwd.allen.service.MyService;

import java.util.Arrays;

/**
 * @author 门那粒沙
 * @create 2020-02-15 21:59
 **/
public class MockitoTest {

    @Test
    public void mockito() {
        MyService myService = Mockito.mock(MyService.class);

        //设置当调用resolveValue并传入"abc"时，返回"efg"
        Mockito.when(myService.resolveValue("abc")).thenReturn("efg");
        System.out.println(myService.resolveValue("abc"));

        //设置当调用printThree时 打印参数
        Mockito.doAnswer(invocationOnMock -> {
            //这里可以获得传给performLogin的参数
            Object[] arguments = invocationOnMock.getArguments();
            System.out.println(Arrays.toString(arguments));
            return null;
        }).when(myService).printThree(ArgumentMatchers.anyString());
        myService.printThree("hello");
    }
}
