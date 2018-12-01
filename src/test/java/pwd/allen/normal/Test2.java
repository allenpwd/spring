package pwd.allen.normal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author pwd
 * @create 2018-10-28 22:22
 **/
public class Test2 {

    @Test
    public void test() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\exportExcel.xls");
        System.out.println(file.getCanonicalFile());
    }

    @Test
    public void test2() {
        try {
            Class clazz = Demo.class;
            Annotation[] annotations = clazz.getAnnotations();
            Annotation annotation = annotations[0];
            System.out.println(annotation.getClass().getName());
            System.out.println(Profile.class.getName());
            Method[] methods = clazz.getMethods();
            Field[] fields = clazz.getFields();

            throw new IOException("adfadf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Profile("abc")
class Demo {
    @Value("str")
    private String str;

    public String getStr() {
        return str;
    }

    @Qualifier("aba")
    public void setStr(String str) {
        this.str = str;
    }
}