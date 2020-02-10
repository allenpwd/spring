package pwd.allen.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 *  自定义异常转义器
 *
 * @author 门那粒沙
 * @create 2020-02-10 10:37
 **/
@Component
public class CustomPersistenceExceptionTranslator implements PersistenceExceptionTranslator {

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        System.out.println("有异常，但我不转：" + ex.toString());
        return null;
    }
}
