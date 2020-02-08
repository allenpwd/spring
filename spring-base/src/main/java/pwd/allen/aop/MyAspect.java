package pwd.allen.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 基于Aspectj注解声明切面类
 * 虽然使用了Aspectj注解，但是底层仍然是靠动态代理技术实现，因此并不依赖于AspectJ的编译器
 * AOP 在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；
 *
 * 通知方法
 *      前置通知@Before：在目标方法运行之前运行
 *      后置通知@After：在目标方法运行之后运行
 *      返回通知@AfterReturning：在目标方法正常返回后运行
 *      异常通知@AfterThrowing：在目标方法出现异常后运行
 *      环绕通知@Around：动态代理，手动推动目标方法运行joinPoint.procced()
 *
 * JoinPoint：织入增强处理的连接点
 *      Object[] getArgs()：返回执行目标方法时的参数。
 *      Signature getSignature()：返回被增强的方法的相关信息。
 *      Object getTarget()：返回被织入增强处理的目标对象。
 *      Object getThis()：返回AOP框架为目标对象生成的代理对象。
 *
 * 步骤
 * 1.导入aop模块；spring-aspects
 * 2.将切面类和业务逻辑类（目标方法所在类）都加入到容器中；
 * 3.必须告诉Spring哪个类是切面类（给切面类上加一个注解：@Aspect）
 * 4.给配置类中加@EnableAspectJAutoProxy（开启基于注解的Aop模式）、XML配置方式：<aop:aspectj-autoproxy/>
 *
 * 注意
 * 1.通知方法可以定义JoinPoint（org.aspectj.lang.JoinPoint，别和org.aopalliance.intercept.Joinpoint混淆）参数
 *  这个参数必须作为第一个参数，否则报错【error at ::0 formal unbound in pointcut】
 * 2.通知注解一定要有value，否则报错【Must set property 'expression' before attempting to match】
 * @author pwd
 * @create 2018-11-22 23:31
 **/
@Component
@Aspect
@Order(1)//定义优先级,值越小优先级越大，如果order一样，测试：xml配置的aspect优先于注解配置的
public class MyAspect {

    /**
     * 设置切入点表达式：pwd.allen.service包下-》My开头的类-》One结尾的所有方法
     *
     * 注意：@Pointcut标注的方法返回值必须是void，但试了下是可以的
     */
    @Pointcut(value = "execution(public void pwd.allen.service.My*.*One(..))")
    public void myPointCut() {}

    /**
     * 设置切入点表达式：bean容器中@MyAnnotation标注的所有方法
     */
    @Pointcut(value = "@annotation(pwd.allen.annotation.MyAnnotation)")
    public void myAnnocationPointCut() {}

    /**
     * 同时满足上面两个表达式的切入点，xml配置方式的话无法实现这种组合
     */
    @Pointcut(value = "myPointCut() && myAnnocationPointCut()")
    public void combinePointCut() {}

    @Before("myPointCut() || myAnnocationPointCut()")
    public void before(JoinPoint joinPoint) {
        System.out.println(String.format("【@Before】%s.%s(%s)", joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName()
                , Arrays.asList(joinPoint.getArgs())));
    }

    /**
     * 业务逻辑退出后（包括正常执行结束和异常退出），执行
     * @param joinPoint
     */
    @After("myPointCut()")
    public void after(JoinPoint joinPoint) {
        System.out.println(String.format("【@After】%s.%s(%s)", joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName()
                , Arrays.asList(joinPoint.getArgs())));
    }

    /**
     * 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
     *
     * 注意：当核心业务抛异常后，立即退出，转向After Advice
     * 执行完毕After Advice，再转到Throwing Advice
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("myPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(String.format("【@Around】%s.%s(%s)", joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName()
                , Arrays.asList(joinPoint.getArgs())));
        Object rel = joinPoint.proceed();
        return rel;
    }

    /**
     * 业务逻辑调用异常退出后，执行
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value="myPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println(String.format("【@afterThrowing】%s.%s(%s)--异常：%s"
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName()
                , Arrays.asList(joinPoint.getArgs())
                , e.getMessage()));
    }

    /**
     * 不管是否有返回值，正常退出后，均执行
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value="combinePointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println(String.format("【@afterThrowing】%s.%s(%s)--返回值：%s"
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName()
                , Arrays.asList(joinPoint.getArgs())
                , result));
    }
}
