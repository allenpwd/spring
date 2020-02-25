package pwd.allen.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringValueResolver;

/**
 * @author 门那粒沙
 * @create 2020-02-25 21:06
 **/
@Configuration
public class BatchConfig2 implements ApplicationContextAware {

    /**
     * 用于向jobRegistry中注册容器中的job
     * @return
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
        beanPostProcessor.setJobRegistry(jobRegistry);
        return beanPostProcessor;
    }

    /**
     * 醉了我就，加入JobRegistryBeanPostProcessor后数据库需要被提前实例化，这个时候容器还没有初始化默认的embeddedValueResolver
     * 只好提前初始化，暂时不知道怎么处理更好 TODO
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ((AbstractApplicationContext) applicationContext).getBeanFactory().addEmbeddedValueResolver(new StringValueResolver() {
            @Override
            public String resolveStringValue(String strVal) {
                return applicationContext.getEnvironment().resolvePlaceholders(strVal);
            }
        });
    }

}
