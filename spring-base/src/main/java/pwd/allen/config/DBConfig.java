package pwd.allen.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据库配置
 *
 * {@link org.springframework.transaction.annotation.Transactional}注解
 *  属性：
 *      propagation：指定事务传播机制，默认 Transaction
 *
 * @author lenovo
 * @create 2020-01-28 22:27
 **/
@EnableTransactionManagement//开启基于注解的声明式事务，可以代替xml的<tx:*>配置
@PropertySource(value = {"classpath:/db.properties"}, encoding = "UTF-8")
@Configuration
public class DBConfig {

    /**
     * @Value从配置文件中解析url
     * @see org.springframework.beans.factory.support.AbstractBeanFactory#resolveEmbeddedValue
     * @param jdbcUrl
     * @param password
     * @return
     * @throws Exception
     */
    @Bean(initMethod = "init")
    public DruidDataSource dataSource(@Value("${jdbc.url}") String jdbcUrl
            , @Value("${jdbc.password}")String password) throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setLoginTimeout(5);
        return dataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }
    @Bean
    public PlatformTransactionManager dataSourceTransactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 异常转义器的后置处理器：会自动查找所有的异常转义器(实现 Persistenceexception Translator接口的
     * bean),并且拦截所有标记为@ Repository注解的bean,通过代理来拦截异常,然后通过 Persisten
     * ceexception Translator将DAO层异常转义后的异常抛出。
     * @return
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
