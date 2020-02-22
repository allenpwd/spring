package pwd.allen.web.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 用来监听Websocket事件
 *
 * @author 门那粒沙
 * @create 2020-02-20 23:06
 **/
@Log4j2
@Component
public class WebSocketEventListener {

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        log.info("Received a new WebSocket connection");
    }

    @EventListener
    public void handleDisConnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    }
}
