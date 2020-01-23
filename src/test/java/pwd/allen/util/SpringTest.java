package pwd.allen.util;

import org.junit.Test;
import org.springframework.test.annotation.Repeat;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author lenovo
 * @create 2020-01-23 10:56
 **/
public class SpringTest {

    @Repeat(5)
    @Test
    public void test() {
        System.out.println("abc");
    }

    @Test
    public void test2() throws IOException {
        RandomAccessFile r = new RandomAccessFile("C:\\Users\\lenovo\\Desktop\\BigDataTwo_Service.log", "r");
        String str_line = null;
        int int_line = 0;
        while (int_line < 100) {
            str_line = r.readLine();
            int_line++;
            System.out.println(str_line);
        }
    }
}
