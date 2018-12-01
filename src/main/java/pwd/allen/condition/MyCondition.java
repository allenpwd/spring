package pwd.allen.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author pwd
 * @create 2018-11-17 8:38
 **/
public class MyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        if (environment.getProperty("os.name").startsWith("Window")) {
            return true;
        }
        return false;
    }
}
