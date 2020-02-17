package pwd.allen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import pwd.allen.websocket.MyWebSocketHandler;

/**
 *
 * @author 门那粒沙
 * @create 2020-02-15 23:14
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/myWebSocketHandler")
                .setAllowedOrigins("*")//跨域设置，允许所有的源
                .addInterceptors(new HttpSessionHandshakeInterceptor());//TODO
        //配置了浏览器不支持WebSocket技术时的替代方案
        registry.addHandler(myWebSocketHandler(), "/myWebSocketHandler_SockJS").withSockJS();
    }

    @Bean
    public MyWebSocketHandler myWebSocketHandler() {
        return new MyWebSocketHandler();
    }

    /**
     * 配置控制运行时特性的属性
     * tomcat、WildFly、GlassFish等
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean servletServerContainer() {
        ServletServerContainerFactoryBean factoryBean = new ServletServerContainerFactoryBean();
        //配置消息缓冲区大小
        factoryBean.setMaxTextMessageBufferSize(8888);
        factoryBean.setMaxBinaryMessageBufferSize(8888);
        return factoryBean;
    }

}

