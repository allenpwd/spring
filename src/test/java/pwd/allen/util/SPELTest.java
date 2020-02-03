package pwd.allen.util;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;
import pwd.allen.config.MainConfig;
import pwd.allen.entity.Person;

import java.util.ArrayList;

/**
 * 测试spring 的 spel表达式解析器
 *
 * @author lenovo
 * @create 2020-01-29 21:24
 **/
public class SPELTest {

    public static ApplicationContext getApplicationContext() {
        return new AnnotationConfigApplicationContext(MainConfig.class);
    }

    @Test
    public void spel() throws NoSuchMethodException {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        String str = null;
        Expression expression = null;

        //测试调用方法和属性
        str = "'hello world!'.bytes.length";
        //相当于getBytes().length
        expression = parser.parseExpression(str);
        System.out.println(str + "=" + expression.getValue());

        //调用静态方法，T()对java.lang包下的类型可以不需要写完整包路径
        str = "T(java.lang.Math).random() * 100.0";
        expression = parser.parseExpression(str);
        System.out.println(str + "=" + expression.getValue());

        //使用默认#{}模版解析，获取环境变量
        str = "#{T(System).getProperty('user.dir')}";
        expression = parser.parseExpression(str, new TemplateParserContext());
        System.out.println(str + "=" + expression.getValue());

        //设置评估上下文
        Person person = new Person();
        person.setName("测试spel");
        ArrayList list = new ArrayList();
        list.add(person);
        evaluationContext.setVariable("person", person);
        evaluationContext.setVariable("method", StringUtils.class.getDeclaredMethod("isEmpty", Object.class));
        evaluationContext.setRootObject(list);//rootObject可以用#root替代，#root可以省略
        str = "#{#person.name} #{[0].name} #{#root[0].name} #{#method('a')}";
        expression = parser.parseExpression(str, new TemplateParserContext());
        System.out.println(str + "=" + expression.getValue(evaluationContext, String.class));

        //安全导航运算符，返回null的方式而不是抛出NullPointException
        str = "#notExist?.name?.age";
        expression = parser.parseExpression(str);
        System.out.println(str + "=" + expression.getValue());

        //Elvis运算符
        str = "#ifExist?:'unknown'";//相当于#ifExist?ifExist:'unknown'
        expression = parser.parseExpression(str);
        System.out.println(str + "=" + expression.getValue());
    }

}
