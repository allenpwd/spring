package pwd.allen.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 门那粒沙
 * @create 2020-02-11 17:54
 **/
@Log4j2
public class MyInterceptor implements HandlerInterceptor {

    /**
     * 在处理方法之前执行，一般用来做一些准备工作：比如日志，权限检查
     * 如果返回false 表示被拦截，将不会执行处理方法
     * 返回true继续执行处理方法
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        log.info("【HandlerInterceptor.preHandle】");
        return true;
    }
    /**
     * 在处理方法执行之后，在渲染视图执行之前执行，一般用来做一些清理工作
     */
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mv)
            throws Exception {
        log.info("【HandlerInterceptor.postHandle】");
    }
    /**
     * 在视图渲染后执行  一般用来释放资源
     * 若preHandle返回false，这个方法也不会被执行
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        log.info("【HandlerInterceptor.afterCompletion】");
    }
}
