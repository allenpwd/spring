package pwd.allen.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串转日期
 *
 * @author 门那粒沙
 * @create 2020-02-22 20:48
 **/
public class StringToDateConverter implements Converter<String, Date> {

    private String datePattern;
    public void setDatePattern(String pattern){
        this.datePattern=pattern;
    }
    @Override
    public Date convert(String date){
        try{
            SimpleDateFormat dateFormat=new SimpleDateFormat(this.datePattern);
            return dateFormat.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("日期转换失败");
            return null;
        }
    }
}
