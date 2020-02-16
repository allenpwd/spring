package pwd.allen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
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
        MyWebSocketHandler webSocketHandler = new MyWebSocketHandler();
        registry.addHandler(webSocketHandler, "/myWebSocketHandler");
        //配置了浏览器不支持WebSocket技术时的替代方案
        registry.addHandler(webSocketHandler, "/myWebSocketHandler_SockJS").withSockJS();
    }

//    @Bean
//    public MyWebSocketHandler myWebSocketHandler() {
//        return new MyWebSocketHandler();
//    }
}

