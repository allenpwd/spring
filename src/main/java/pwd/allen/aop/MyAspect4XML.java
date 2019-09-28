package pwd.allen.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
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
 * 1.通知方法可以定义JoinPoint（org.aspectj.lang.JoinPoint，别和org.aopalliance.intercept.Joinpoint混淆）参数，这个参数必须作为第一个参数，否则报错【error at ::0 formal unbound in pointcut】
 * 2.通知注解一定要有value，否则报错【Must set property 'expression' before attempting to match】
 * @author pwd
 * @create 2018-11-22 23:31
 **/
public class MyAspect4XML {

    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("【@Before】" + joinPoint.getTarget().getClass().getName()
                + "." +joinPoint.getSignature().getName()
                + "--参数：" + Arrays.asList(args));
    }

    /**
     * 业务逻辑退出后（包括正常执行结束和异常退出），执行
     * @param joinPoint
     */
    public void after(JoinPoint joinPoint) {
        System.out.println("【@After】" + joinPoint.getTarget().getClass().getName()
                + "." +joinPoint.getSignature().getName());
    }

    /**
     * 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
     *      *
     *      * 注意：当核心业务抛异常后，立即退出，转向After Advice
     *      * 执行完毕After Advice，再转到Throwing Advice
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("【@Around】" + joinPoint.getTarget().getClass().getName()
                + "." +joinPoint.getSignature().getName());
        Object rel = joinPoint.proceed();
        return rel;
    }

    /**
     * 业务逻辑调用异常退出后，执行
     * @param joinPoint
     * @param e
     */
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("【@afterThrowing】" + joinPoint.getTarget().getClass().getName()
                + "." +joinPoint.getSignature().getName()
        + "--异常：" + e.getMessage());
    }

    /**
     * 不管是否有返回值，正常退出后，均执行
     * @param joinPoint
     * @param result
     */
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("【@afterReturning】" + joinPoint.getTarget().getClass().getName()
                + "." +joinPoint.getSignature().getName() + "--返回值：" + result);
    }
}
