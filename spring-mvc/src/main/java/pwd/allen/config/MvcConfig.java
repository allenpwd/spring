package pwd.allen.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.support.*;
import org.springframework.web.util.UrlPathHelper;
import pwd.allen.interceptor.MyInterceptor;

import java.io.IOException;

/**
 * spring MVC配置类
 *
 * 容器配置的方式：
 * 	1）基于web.xml
 * 	2）基于java配置类：继承 {@link AbstractAnnotationConfigDispatcherServletInitializer} 设置根容器和子容器的配置类、servletMappings等
 * 	3）基于xml配置：继承 {@link AbstractDispatcherServletInitializer}
 *
 */
@Configuration
@ComponentScan(basePackages="pwd.allen.controller")
@Import({AOPConfig.class})//把AOP配置放到spring mvc容器里，不然自定义的AOP在controller层不起效
@EnableWebMvc//等价于xml配置中的<mvc:annotation-driven />，原理：引入WebMvcConfigurationSupport实现
public class MvcConfig extends WebMvcConfigurerAdapter {

	/**
	 * 配置视图解析器
	 * @return
	 */
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

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		//开启 matrix-variables 特性，默认为true不开启
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);

		super.configurePathMatch(configurer);
	}

	/**
	 * TODO 网上说可以在这里处理跨域
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		super.addCorsMappings(registry);
	}

	/**
	 * 添加拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyInterceptor());
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

}
