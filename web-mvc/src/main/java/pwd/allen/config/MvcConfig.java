package pwd.allen.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.*;
import java.io.IOException;

/**
 * spring MVC配置类
 *
 * WebApplicationInitializer：可以看做是Web.xml的替代，在其中可以添加servlet，listener等，在加载Web项目的时候会加载这个接口实现类
 * 	原理：SpringServletContainerInitializer通过SPI机制在web容器启动时被实例化并调用，
 * 	调用的结果就是得到WebApplicationInitializer子类，逐个实例化并调用他们的onStartup方法
 * 	@see org.springframework.web.SpringServletContainerInitializer
 * 	@see javax.servlet.ServletContainerInitializer
 *
 */
@Configuration
@ComponentScan(basePackages="pwd.allen.controller")
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter implements WebApplicationInitializer {

	@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(1);
		return resolver;
	}

	/**
	 * 配置静态资源
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}

	/**
	 * 配置验证码生产工具
	 * @return
	 * @throws IOException
	 */
	@Bean
	public DefaultKaptcha kaptcha() throws IOException {
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		Config config = new Config(PropertiesLoaderUtils.loadProperties(new ClassPathResource("kaptcha.properties")));
		kaptcha.setConfig(config);
		return kaptcha;
	}


	/**
	 * 将在web容器加载时被调用，可以替代web.xml的功能
	 * @param servletContext
	 * @throws ServletException
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		//region 配置druid的监控功能
		ServletRegistration.Dynamic dynamic = servletContext.addServlet("statViewServlet", StatViewServlet.class);
		dynamic.setInitParameter("loginUsername", "druid");
		dynamic.setInitParameter("loginPassword", "123456");
		dynamic.setInitParameter("resetEnable", "false");
		dynamic.addMapping("/druid/*");

		FilterRegistration.Dynamic filterDynamic = servletContext.addFilter("webStatFilter", WebStatFilter.class);
		filterDynamic.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterDynamic.addMappingForUrlPatterns(null, false, "/*");
		//endregion
	}
}
