package pwd.allen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.util.UrlPathHelper;
import pwd.allen.interceptor.MyInterceptor;

/**
 * spring MVC配置类
 *
 * 容器配置的方式：
 * 	1）基于web.xml
 * 	2）基于java配置类：继承 {@link AbstractAnnotationConfigDispatcherServletInitializer} 设置根容器和子容器的配置类、servletMappings等
 * 	3）基于xml配置：继承 {@link AbstractDispatcherServletInitializer}
 *
 */
@EnableAsync//开启@Async注解异步处理
@Configuration
@ComponentScan(basePackages="pwd.allen.controller")
@Import({AOPConfig.class, BeanConfig.class})//把AOP配置放到spring mvc容器里，不然自定义的AOP在controller层不起效
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
	 * TODO 不知道怎么开启
	 * @param configurer
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		super.configureAsyncSupport(configurer);
	}
}
