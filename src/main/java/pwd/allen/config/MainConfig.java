package pwd.allen.config;

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

@EnableTransactionManagement//声明式事务
@EnableAspectJAutoProxy//注册AnnotationAwareAspectJAutoProxyCreator
@PropertySource(value = {"classpath:/my.properies"}, encoding = "UTF-8")//加载指定的配置文件
@ComponentScan(value = {"pwd.allen"},
        excludeFilters = {
            @ComponentScan.Filter(type=FilterType.ANNOTATION, value={Controller.class})
        })//自动扫描pwd.allen目录下的组件，并排除@Controller标注的
@Import({MyService.class, MyImportSelector.class})
@ImportResource({"classpath:bean.xml"})//导入Spring的XML配置文件 可参考ConfigurationClassBeanDefinitionReader的loadBeanDefinitionsFromImportedResources方法
@Configuration
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
