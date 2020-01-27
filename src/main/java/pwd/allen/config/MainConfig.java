package pwd.allen.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
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

@EnableTransactionManagement//声明式事务
@EnableAspectJAutoProxy//注册AnnotationAwareAspectJAutoProxyCreator，没有的话@Aspect标注的AOP类无法生效
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


    @Bean
    public DataSource dataSource() throws Exception {
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/test";
        String password = "123456";

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword(password);
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setLoginTimeout(5);
        dataSource.setUnreturnedConnectionTimeout(5);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager dataSourceTransactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

}
