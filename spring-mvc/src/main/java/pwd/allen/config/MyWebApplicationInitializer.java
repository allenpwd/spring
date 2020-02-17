package pwd.allen.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.web.WebApplicationInitializer;
import pwd.allen.filter.MyFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * WebApplicationInitializer：可以看做是Web.xml的替代，在其中可以添加servlet，listener等，在加载Web项目的时候会加载这个接口实现类
 * 	原理：SpringServletContainerInitializer通过SPI机制在web容器启动时被实例化并调用，
 * 	调用的结果就是得到WebApplicationInitializer子类，逐个实例化并调用他们的onStartup方法
 * 	@see org.springframework.web.SpringServletContainerInitializer
 * 	@see javax.servlet.ServletContainerInitializer
 *
 * @author 门那粒沙
 * @create 2020-02-11 10:47
 **/
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    /**
     * 将在web容器加载时被调用，可以替代web.xml的功能
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        //region 配置druid的监控功能，springboot可以使用 ServletRegistrationBean 和 FilterRegistrationBean
        //配置DruidDataSource的bean的时候需要指明initMethod="init"使之初始化，否则要第一次sql请求才能看到数据源监控数据
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("statViewServlet", StatViewServlet.class);
        dynamic.setInitParameter("loginUsername", "druid");
        dynamic.setInitParameter("loginPassword", "123456");
        dynamic.setInitParameter("resetEnable", "false");
        dynamic.addMapping("/druid/*");

        FilterRegistration.Dynamic filterDynamic = servletContext.addFilter("webStatFilter", WebStatFilter.class);
        filterDynamic.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterDynamic.setAsyncSupported(true);
        filterDynamic.addMappingForUrlPatterns(null, false, "/*");
        //endregion

        //添加自定义的过滤器
        servletContext.addFilter("myFilter", MyFilter.class).addMappingForUrlPatterns(null, false, "/person/*");

    }
}
