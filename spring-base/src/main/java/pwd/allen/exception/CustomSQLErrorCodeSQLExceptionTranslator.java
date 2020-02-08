package pwd.allen.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;

/**
 * 使用ExceptionTranslator 实现 {@link SQLException} 和 spring自己的{@link DataAccessException}之间的转换
 * @author 门那粒沙
 * @create 2020-02-08 22:29
 **/
public class CustomSQLErrorCodeSQLExceptionTranslator extends SQLErrorCodeSQLExceptionTranslator {
    @Override
    public DataAccessException translate(String task, String sql, SQLException ex) {
        if (ex.getErrorCode() == 1054) {
//            return new BadSqlGrammarException(task, sql, ex);
            task = "oh my god 德[errorCode:1054]" + task;
        }
        return super.translate(task, sql, ex);
    }
}
