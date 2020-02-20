package pwd.allen.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import pwd.allen.web.websocket.MyChannelInterceptor;

/**
 * 使用STOMP子协议作为cs通信的通用格式
 * 特点：
 *  多方使用可拓展性强（通过订阅广播自定义）
 *  STOMP协议为浏览器和server间的通信增加适当的消息语义
 *
 * @author 门那粒沙
 * @create 2020-02-15 23:14
 **/
@Configuration
@EnableWebSocketMessageBroker//启用由消息代理支持的WebSocket消息处理
public class WebSocketConfig2 extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/broker")//注册端点，STOMP客户端连接的地址
                .withSockJS();//启动SockJS,以便在WebSocket不可用时可以使用备用传输
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //启用一个简单的基于内存的消息代理，指定服务端广播消息的路径前缀
        //也可以用成其他的传统信息代理，比如rabbitmq activeMq
        registry.enableSimpleBroker("/topic");

        //指定服务端处理WebSocket消息的前缀是/app
        //如果客户端向/app/hello这个地址发送消息，那么服务端通过@MessageMapping(“/hello”)这个注解来接收并处理消息
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(myChannelInterceptor());
    }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        //理论上Web Socket消息的大小几乎是无限的,但实际上Websocket服务器会施加限制
        // 如 Tomcat上的5KB和Jety上的64KB。因此, STOMP客户端(如 Javascript webstomp-client等)在16KB边界
        //处分割较大的 STOMP消息、并将它们作为多个 Websocket消息发送,需要服务器进行缓冲和重新组装。
        registration.setSendBufferSizeLimit(8888)//配置向客户端发送消息时可以缓存多少数据
                .setSendTimeLimit(1000 * 10)//配置向客户端发送消息的时间限制，单位毫秒
                .setMessageSizeLimit(1024 * 128);//配置Stomp最大容量
    }



    @Bean
    public MyChannelInterceptor myChannelInterceptor() {
        return new MyChannelInterceptor();
    }
}

