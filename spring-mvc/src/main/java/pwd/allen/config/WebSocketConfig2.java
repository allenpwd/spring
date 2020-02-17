package pwd.allen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * 使用欧冠STOMP子协议作为cs通信的通用格式
 *
 * @author 门那粒沙
 * @create 2020-02-15 23:14
 **/
@Configuration
@EnableWebSocketMessageBroker//启用由消息代理支持的WebSocket消息处理
public class WebSocketConfig2 extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/broker")//注册端点，websocket客户端通过它连接
                .withSockJS();//启动SockJS,以便在WebSocket不可用时可以使用备用传输
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //启用一个简单的基于内存的消息代理，
        //也可以用成其他的传统信息代理，比如rabbitmq activeMq
        registry.enableSimpleBroker("/topic", "/queue");
        //如果destination前缀为/app则交给能匹配/app后面路径的方法去处理
        registry.setApplicationDestinationPrefixes("/app");
    }
}

