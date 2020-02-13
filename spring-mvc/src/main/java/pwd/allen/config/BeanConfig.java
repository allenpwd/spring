package pwd.allen.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;

/**
 * @author 门那粒沙
 * @create 2020-02-12 17:21
 **/
@Configuration
public class BeanConfig {

    /**
     * 配置验证码生产工具
     * @return
     * @throws IOException
     */
    @Bean
    public DefaultKaptcha kaptcha() throws IOException {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(PropertiesLoaderUtils.loadProperties(new ClassPathResource("kaptcha.properties")));
        kaptcha.setConfig(config);
        return kaptcha;
    }

    /**
     * 文件上传
     *
     * @return
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024 * 20);	//20kb
        return multipartResolver;
    }
}
