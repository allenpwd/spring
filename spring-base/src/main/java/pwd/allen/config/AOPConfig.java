package pwd.allen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP配置
 *
 * @EnableAspectJAutoProxy
 *  作用：注册AnnotationAwareAspectJAutoProxyCreator（一个Bean后处理器，为容器中的Bean生成AOP代理），相当于xml配置中：<aop:aspectj-autoproxy />
 *  属性：
 *      proxyTargetClass：为true时强制使用CGLIB代理，否则目标类有实现接口的话就用jdk动态代理
 *      exposeProxy：若为true，可通过AopContext.currentProxy()获取当前代理对象
 *
 * @author lenovo
 * @create 2020-01-28 22:25
 **/
@EnableAspectJAutoProxy(proxyTargetClass = false, exposeProxy = true)//没有的话@Aspect标注的AOP类无法生效
@Configuration
public class AOPConfig {
}
