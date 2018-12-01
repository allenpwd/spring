package pwd.allen.aop;

import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**切面类
 * AOP 在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；
 * 通知方法
 *      前置通知@Before：在目标方法运行之前运行
 *      后置通知@After：在目标方法运行之后运行
 *      返回通知@AfterReturning：在目标方法正常返回后运行
 *      异常通知@AfterThrowing：在目标方法出现异常后运行
 *      环绕通知@Around：动态代理，手动推动目标方法运行joinPoint.procced()
 *
 * 步骤
 * 1.导入aop模块；Spring AOP(spring-aspects)
 * 2.将切面类和业务逻辑类（目标方法所在类）都加入到容器中；
 * 3.必须告诉Spring哪个类是切面类（给切面类上加一个注解：@Aspect）
 * 4.给配置类中加@EnableAspectJAutoProxy（开启基于注解的Aop模式）
 *
 * 注意
 * 1.通知方法可以参入JoinPoint且必须在第一位，否则报错【error at ::0 formal unbound in pointcut】
 * 2.通知注解一定要有value，否则报错【Must set property 'expression' before attempting to match】
 * @author pwd
 * @create 2018-11-22 23:31
 **/
@Component
@Aspect
public class MyAspect {

    //抽取公共的切入点表达式
    @Pointcut(value = "execution(public void pwd.allen.service.MyService.*(..))")
    public void myPointCut() {}

    @Before("myPointCut()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("【@Before】" + joinPoint.getSignature().getName()
                + "--参数：" + Arrays.asList(args));
    }

    @After("myPointCut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("【@After】" + joinPoint.getSignature().getName());
    }

    @AfterThrowing(value="myPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("【@afterThrowing】" + joinPoint.getSignature().getName()
        + "--异常：" + e.getMessage());
    }

    @AfterReturning(value="myPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("【@afterReturning】" + joinPoint.getSignature().getName() + "--返回值：" + result);
    }
}
