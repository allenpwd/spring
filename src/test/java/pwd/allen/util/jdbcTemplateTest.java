package pwd.allen.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import pwd.allen.config.MainConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lenovo
 * @create 2020-01-21 16:53
 **/
@RunWith(SpringJUnit4ClassRunner.class)//指定单元测试执行类
@ContextConfiguration(classes = MainConfig.class)
public class jdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String myStr;

    @BeforeTransaction
    public void beforeTransaction() {
        System.out.println("beforeTransaction");
    }

    @AfterTransaction
    public void afterTransaction() {
        System.out.println("afterTransaction");
    }

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

        System.out.println(myStr);
    }

    /**
     * 测试给blob类型字段赋值
     */
    @Test
    @Transactional
    public void setBlob() {
        String sql = "UPDATE db_user set msg=? where id=?";
        final LobHandler lobHandler = new DefaultLobHandler();

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
}
