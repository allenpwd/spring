package util;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.nio.charset.Charset;

/**
 * @author 门那粒沙
 * @create 2020-02-18 21:12
 **/
public class StompTest {

    @Test
    public void test() throws InterruptedException {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter(Charset.forName("utf-8")));

        String url = "ws://localhost:8080/broker";
        stompClient.connect(url, new MyStompSessionHandler());

//        Thread.sleep(10000000);
    }
}


@Log4j2
class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("与服务器建立连接：{}", session);
        session.send("/app/control", "我是客户端");
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        super.handleFrame(headers, payload);
    }
}