package pwd.allen.annotation;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pwd.allen.aop.MyAspect;
import pwd.allen.condition.MyCondition;
import pwd.allen.entity.Fruit;
import pwd.allen.importBeanDefinitionRegistrar.MyImportBeanDefinitionRegistrar;
import pwd.allen.importSelector.MyImportSelector;
import pwd.allen.service.MyService;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Date;

/**
 * 声明式事务
 */
@EnableTransactionManagement
/**
 * 注册AnnotationAwareAspectJAutoProxyCreator
 */
@EnableAspectJAutoProxy
@PropertySource(value = {"classpath:/my.properies"}, encoding = "UTF-8")
@Configuration
@ComponentScan(value = {"pwd.allen"},
        excludeFilters = {@ComponentScan.Filter(type=FilterType.ANNOTATION, value={Controller.class})})
@Import({MyService.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class MainConfig {

    @Conditional({MyCondition.class})
    @Bean(value = "fruit", initMethod = "init")
    public Fruit fruit() {
        System.out.println("创建fruit实例");
        Fruit fruit = new Fruit();
        fruit.setCreateAt(new Date());
        fruit.setName("猕猴桃");
        return fruit;
    }

    @Bean
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("123");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3307/test");
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
        return  new DataSourceTransactionManager(dataSource());
    }

}
