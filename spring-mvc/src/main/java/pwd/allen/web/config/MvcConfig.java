package pwd.allen.web.config;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.UrlPathHelper;
import pwd.allen.config.AOPConfig;
import pwd.allen.config.MyPropertyEditorRegistrar;
import pwd.allen.web.interceptor.MyInterceptor;

import java.util.concurrent.TimeUnit;

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
@ComponentScan(basePackages="pwd.allen.web")
@Import({AOPConfig.class})//把AOP配置放到spring mvc容器里，不然自定义的AOP在controller层不起效
@EnableWebMvc//等价于xml配置中的<mvc:annotation-driven />，原理：引入WebMvcConfigurationSupport实现
public class MvcConfig extends WebMvcConfigurerAdapter {

	//region 配置视图解析器
	/**
	 * 配置视图解析器
	 *
	 * 被configureViewResolvers方法的registry.jsp()替代
	 *
	 * @return
	 */
	/*@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(1);
		return resolver;
	}*/
	/**
	 * 配置视图解析器
	 *  TODO
	 * @param registry
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.enableContentNegotiation(new MappingJackson2JsonView());
		registry.jsp();
	}
	//endregion


	//<editor-fold desc="静态资源配置">
	/**
	 * 由Spring MVC框架自己处理静态资源，更灵活
	 * 优点：允许静态资源放在任何地方，如WEB-INF目录下、类路径下（包括引入的jar类路径下）等
	 *
	 * @see org.springframework.web.servlet.resource.ResourceHttpRequestHandler
	 *
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("/static/", "classpath:/META-INF/resources/")//最好路径以/结尾，否则可能404
				.setCachePeriod(3600 * 24 * 365) //TODO 这个有什么卵用
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));//静态资源的Cache-Control响应头设置
	}
	/**
	 * 将静态资源的处理经由Spring MVC框架交回Web应用服务器的默认servlet（可以指定servletName，默认default）处理
	 * xml配置方式：<mvc:default-servlet-handler/>
	 * 效果：WEB-INF目录下的静态资源可以直接访问
	 * 原理：引入 {@link org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler}
	 * 缺点：静态资源只能放在Web容器的根路径下
	 *
	 * @param configurer
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable("default");
	}
	//</editor-fold>


	/**
	 * 路径匹配
	 * @param configurer
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		//开启 matrix-variables 特性，默认为true不开启
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}

	/**
	 * 可以在这里处理跨域
	 * 可以用@CrossOrigin更细粒度地在类或者方法级别上定义
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/my/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST")
				.maxAge(3600);
	}

	/**
	 * 配置类型转换
	 * @param registry
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		super.addFormatters(registry);
	}

	/**
	 * 添加拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/my/**");
	}

	/**
	 * TODO 配置异步拦截器等
	 * @param configurer
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		super.configureAsyncSupport(configurer);
	}

	/**
	 * 视图控制器
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("page").setViewName("jsp/page");
	}

}
