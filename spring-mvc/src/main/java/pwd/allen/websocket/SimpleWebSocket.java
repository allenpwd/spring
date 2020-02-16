package pwd.allen.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 直接使用Java提供的@ServerEndpoint注解实现WebSocket
 * 对web服务器的要求 tomcat:8+ jetty:9+
 *
 * 疑问：如果引用Springmvc，则要加入容器中才有效
 *
 * @author 门那粒沙
 * @create 2020-02-16 19:06
 **/
@Component
@Log4j2
@ServerEndpoint("/simpleWebSocket")
public class SimpleWebSocket {

    /**
     * 在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 记录所有连接的WebSocket
     */
    private static CopyOnWriteArraySet<SimpleWebSocket> webSockets = new CopyOnWriteArraySet<>();

    /**
     * 当前建立连接的会话
     */
    private Session session;

    /**
     * 连接建立成功后回调
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        onlineCount.incrementAndGet();
        log.info("有webSocket连接建立！当前在线连接{}", onlineCount.get());
    }

    /**
     * 连接关闭后回调
     */
    @OnClose
    public  void onClose() {
        webSockets.remove(this);
        onlineCount.decrementAndGet();
        log.info("有webSocket连接关闭！当前在线连接{}", onlineCount.get());
    }

    /**
     * 收到客户端消息后回调
     * @param message 客户端发过来的消息
     * @param session 可选参数，当前连接会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}", message);

        //群发消息
        for (SimpleWebSocket webSocket : webSockets) {
            try {
                webSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误后回调
     * @param error 异常
     * @param session 可选参数，当前连接会话
     */
    @OnError
    public void onError(Throwable error, Session session) {
        log.info("发生错误:{}", error.toString());
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
//        this.session.getAsyncRemote().sendText(message);
    }
}
