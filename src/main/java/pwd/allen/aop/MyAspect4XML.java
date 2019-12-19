package pwd.allen.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

/**切面类
 * XML的形式配置Aspect
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

        String format = String.format("【%s-Before】%s.%s--参数：%s", this.getClass().getName()
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName(), Arrays.asList(args));
        System.out.println(format);
    }

    /**
     * 业务逻辑退出后（包括正常执行结束和异常退出），执行
     * @param joinPoint
     */
    public void after(JoinPoint joinPoint) {
        String format = String.format("【%s-After】%s.%s", this.getClass().getName()
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName());
        System.out.println(format);
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
        String format = String.format("【%s-Around】%s.%s", this.getClass().getName()
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName());
        System.out.println(format);
        Object rel = joinPoint.proceed();
        return rel;
    }

    /**
     * 业务逻辑调用异常退出后，执行
     * @param joinPoint
     * @param e
     */
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        String format = String.format("【%s-afterThrowing】%s.%s--异常：%s", this.getClass().getName()
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName(), e.getMessage());
        System.out.println(format);
    }

    /**
     * 不管是否有返回值，正常退出后，均执行
     * @param joinPoint
     * @param result
     */
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String format = String.format("【%s-afterReturning】%s.%s--返回值：%s", this.getClass().getName()
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName(), result);
        System.out.println(format);
    }
}
