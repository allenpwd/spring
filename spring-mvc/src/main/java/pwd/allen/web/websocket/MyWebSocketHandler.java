package pwd.allen.web.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 使用Spring提供的低层级WebSocket API实现WebSocket
 * 简单搞一个接收text的
 * 条件：引入spring-websocket.jar spring4.0+
 *
 * @author 门那粒沙
 * @create 2020-02-16 22:35
 **/
@Log4j2
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket有新连接: {}", session.getUri());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("来自客户端的消息：{}", message.getPayload());
        session.sendMessage(new TextMessage("你个大傻叉"));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("websocket发生异常：{}", exception.toString());
//        if (session.isOpen()) session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket连接关闭：{}", status);
    }
}
