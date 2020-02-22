package pwd.allen.config;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PropertyEditor注册器，注册一个支持yyyy-MM-dd格式的字符串赋值给Date类型属性的属性编辑器
 * 用法：注册进BeanFactory，bean工厂在创建每个bean时会先创建BeanWrapper来包装bean，这个BeanWrapper会加入各种工具包括PropertyEditor
 *
 * @author 门那粒沙
 * @create 2020-02-22 17:23
 **/
public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
