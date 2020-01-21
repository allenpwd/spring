package pwd.allen.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfig.class)
public class jdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试查询blob类型的字段
     * @throws UnsupportedEncodingException
     */
    @Test
    public void test() throws UnsupportedEncodingException {
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

    @Test
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
