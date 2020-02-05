package pwd.allen.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import pwd.allen.condition.MyCondition;
import pwd.allen.entity.Fruit;
import pwd.allen.importSelector.MyImportSelector;
import pwd.allen.service.MyService;

import java.util.Date;

//默认的profile是default，若没有设置profile则该配置类被激活
//要激活的profile可通过
@Profile("default")
@PropertySource(value = {"classpath:my.properties"}, encoding = "UTF-8")//加载指定的配置文件，如果其他项目依赖这个项目，path前加上/的话会报找不到资源的错误
//自动扫描pwd.allen目录下的组件，并排除@Controller标注的
@ComponentScan(value = {"pwd.allen"},
        excludeFilters = {
            @ComponentScan.Filter(type=FilterType.ANNOTATION, value={Controller.class, Configuration.class})
        })
@Import({MyService.class, MyImportSelector.class, DBConfig.class})
//导入Spring的XML配置文件 可参考ConfigurationClassBeanDefinitionReader的loadBeanDefinitionsFromImportedResources方法
@ImportResource({"classpath:bean.xml"})
@Configuration
public class MainConfig {

//    @Primary//Primary只能标注一个，否则报错：more than one 'primary' bean found among candidates
    @Qualifier("fruit")//加上这个的话会被MyService的fruits自动注入
    @Conditional({MyCondition.class})
    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Description("bean注解创建fruit实例")
    public Fruit fruitFromAnnotation() {
        System.out.println("bean注解创建fruit实例");
        Fruit fruit = new Fruit();
        fruit.setCreateAt(new Date());
        fruit.setName("注解方式创建");
        return fruit;
    }

    /**
     * 国际化资源配置
     *
     * 如果没有自己定义一个，容器会默认创建一个 {@link org.springframework.context.support.DelegatingMessageSource}
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //指定类路径message目录下test这个资源包
        messageSource.setBasename("message.test");
        return messageSource;
    }
}
