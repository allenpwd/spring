package pwd.allen.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pwd.allen.annotation.MyQualifier;
import pwd.allen.aop.MyAspect;
import pwd.allen.condition.MyCondition;
import pwd.allen.entity.Fruit;
import pwd.allen.entity.Person;
import pwd.allen.importBeanDefinitionRegistrar.MyImportBeanDefinitionRegistrar;
import pwd.allen.importSelector.MyImportSelector;
import pwd.allen.service.MyService;

import javax.sql.DataSource;
import java.beans.ConstructorProperties;
import java.beans.PropertyVetoException;
import java.util.Date;

//默认的profile是default，若没有设置profile则该配置类被激活
//要激活的profile可通过
@Profile("default")
@PropertySource(value = {"classpath:/my.properies"}, encoding = "UTF-8")//加载指定的配置文件
//自动扫描pwd.allen目录下的组件，并排除@Controller标注的
@ComponentScan(value = {"pwd.allen"},
        excludeFilters = {
            @ComponentScan.Filter(type=FilterType.ANNOTATION, value={Controller.class})
        })
@Import({MyService.class, MyImportSelector.class})
//导入Spring的XML配置文件 可参考ConfigurationClassBeanDefinitionReader的loadBeanDefinitionsFromImportedResources方法
@ImportResource({"classpath:bean.xml"})
@Configuration
public class MainConfig {

//    @Primary//Primary只能标注一个，否则报错：more than one 'primary' bean found among candidates
    @Qualifier("fruit")//加上这个的话会被MyService的fruits自动注入
    @Conditional({MyCondition.class})
    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Description("bean注解创建fruit实例")
    public Fruit fruitFromAnnotation() {
        System.out.println("bean注解创建fruit实例");
        Fruit fruit = new Fruit();
        fruit.setCreateAt(new Date());
        fruit.setName("注解方式创建");
        return fruit;
    }

    /**
     * 如果没有自己定义一个，容器会默认创建一个 {@link org.springframework.context.support.DelegatingMessageSource}
     *
     * 国际化
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //指定类路径message目录下test这个资源包
        messageSource.setBasename("message.test");
        return messageSource;
    }
}
