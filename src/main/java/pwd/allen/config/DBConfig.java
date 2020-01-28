package pwd.allen.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
public class DBConfig {

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
