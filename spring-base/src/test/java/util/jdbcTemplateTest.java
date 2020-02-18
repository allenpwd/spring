package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.FileCopyUtils;
import pwd.allen.config.MainConfig;
import pwd.allen.entity.Person;
import pwd.allen.exception.CustomSQLErrorCodeSQLExceptionTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 测试 {@link JdbcTemplate}
 *
 * 事务管理
 * TestContext框架默认会配置 {@link TransactionalTestExecutionListener}来支持事务管理，
 *  但是还需要在上下文里配置一个 {@link org.springframework.transaction.PlatformTransactionManager}的bean
 *  并在类或方法级别声明@Transactional注解
 * @Tranactional
 *  标注在类上：类的每个测试方法将在同一个事务中运行
 *  传播类型设置为NOT_SUPPORTED，则不在事务中运行
 *
 * @Sql 声明式执行sql脚本
 *  若没有指定脚本
 *      1）标注在类上，默认脚本为classpath:测试类包路径/类名.sql
 *      2）标注在方法上，默认脚本为classpath:测试类路径/类名.方法名.sql
 *
 * @author lenovo
 * @create 2020-01-21 16:53
 **/
@RunWith(SpringJUnit4ClassRunner.class)//指定单元测试执行类
@ContextConfiguration(classes = {MainConfig.class})
public class jdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 事务开始之前执行
     * 例子：测试之前验证初始数据库状态
     */
    @BeforeTransaction
    public void beforeTransaction() {
        System.out.println("beforeTransaction");
    }

    /**
     * 事务开始之后执行
     * 例子：测试执行之后验证预期的事务性执行行为
     */
    @AfterTransaction
    public void afterTransaction() {
        System.out.println("afterTransaction");
    }

    /**
     * @Sql 执行初始化sql，由 {@link org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener}支持
     */
    @Test
//    @Transactional//加了的话插入的数据会被回滚，只剩空表
    @Sql(scripts = {"/sql/db_user.sql"}, config = @SqlConfig(commentPrefix = "--", separator = ";"))
    public void init() {}

    /**
     * 测试查询blob类型的字段
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getBlob() throws UnsupportedEncodingException {
        String sql = "select * from db_user where id= ?";
        final String field = "msg";
        Object id = 1;

        final LobHandler lobHandler = new DefaultLobHandler();
        final ByteArrayOutputStream contentOs = new ByteArrayOutputStream();
        String val = null;
        jdbcTemplate.query(sql, new Object[]{id}, new AbstractLobStreamingResultSetExtractor() {
                    public void streamData(ResultSet rs) throws SQLException, IOException {
                        FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, field), contentOs);
                    }
                }
        );
        val = new String(contentOs.toByteArray(), "UTF-8");

        System.out.println(val);
    }

    /**
     * 测试给blob类型字段赋值
     * 使用注解方式加上事务（推荐）事务管理与业务逻辑解耦
     */
    @Test
    @Transactional
    public void setBlob() {
        String sql = "UPDATE db_user set msg=? where id=?";
        final LobHandler lobHandler = new DefaultLobHandler();

        System.out.println("当前所在事务：" + TransactionSynchronizationManager.getCurrentTransactionName());

        final byte[] val = "你是傻逼".getBytes();
        Integer int_rel = jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                lobCreator.setBlobAsBytes(ps, 1, val);
                ps.setInt(2, 1);
            }
        });
        System.out.println(int_rel);
    }

    /**
     * 用 TransactionTemplate加上事务，
     * 原理：从源码可以看到其实是调用transactionManager.getTransaction(this)获取事务管理，本身自己继承TransactionDefinition，可以定义事务属性
     * 特点：能明确地设置事务，与逻辑代码紧密结合，适合事务操作比较少不繁琐的场景
     */
    @Test
    public void insert() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        String sql = "insert into db_user(user_name,age) values(?,?)";

        Integer int_rel = transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
                status.setRollbackOnly();//设置直接回滚
                return jdbcTemplate.update(sql,"测试插入", 11);
            }
        });
        System.out.println(int_rel);
    }

    /**
     * 测试ExceptionTranslator，自定义封装SQLException
     */
    @Test
    public void exceptionTranslator() {
        jdbcTemplate.setExceptionTranslator(new CustomSQLErrorCodeSQLExceptionTranslator());

        String sql = "select count(1) from db_user where ids=?";//故意把字段写错，会抛出 com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException 错误
        try {
            System.out.println(jdbcTemplate.queryForObject(sql, new Object[]{1}, Integer.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试批处理，多条sql在同个连接中执行
     */
    @Test
    public void batchUpdate() {
        ArrayList<Person> list = new ArrayList<>();
        list.add(new Person(2, "多莫格", 23));
        list.add(new Person(3, "摩洛哥", 24));

        int[] arr_rel = null;

        //方式一
        String sql = "insert into db_user(id,user_name,age) values(?,?,?) " +
                "on DUPLICATE KEY UPDATE user_name=VALUES(user_name),age=VALUES(age)";
        arr_rel = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, list.get(i).getId());
                ps.setString(2, list.get(i).getUserName());
                ps.setInt(3, list.get(i).getAge());
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
        System.out.println(Arrays.toString(arr_rel));

        //方式二 根据bean（setter方法）或者map创建的 SqlParameterSource[]作为参数
        sql = "insert into db_user(id,user_name,age) values(:id,:userName,:age) " +
                "on DUPLICATE KEY UPDATE user_name=VALUES(user_name),age=VALUES(age)";
        arr_rel = new NamedParameterJdbcTemplate(jdbcTemplate).batchUpdate(sql, SqlParameterSourceUtils.createBatch(list.toArray()));
        System.out.println(Arrays.toString(arr_rel));
    }

}
