package pwd.allen.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据库配置
 *
 * @author lenovo
 * @create 2020-01-28 22:27
 **/
@EnableTransactionManagement//声明式事务
@PropertySource(value = {"classpath:/db.properties"}, encoding = "UTF-8")
@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(@Value("${jdbc.url}") String jdbcUrl
            , @Value("${jdbc.password}")String password) throws Exception {
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
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }
    @Bean
    public PlatformTransactionManager dataSourceTransactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
