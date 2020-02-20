package util;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author 门那粒沙
 * @create 2020-02-18 21:12
 **/
public class StompTest {

    /**
     * 使用stomp客户端去请求 服务端的websocket接口/broker，并提交数据到，destination为/app/broadcast
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void test() throws InterruptedException, ExecutionException {

        String url = "ws://localhost:8080/broker";
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        //设置对应的解码器，理论支持任意的pojo自带转json格式发送，这里只使用字节方式发送和接收数据
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter(Charset.forName("utf-8")));
        stompClient.setTaskScheduler(new DefaultManagedTaskScheduler()); // for heartbeats


        System.out.println(stompClient.isRunning());

        ListenableFuture<StompSession> future = stompClient.connect(url, new MyStompSessionHandler());
        StompSession stompSession = future.get();
        stompSession.setAutoReceipt(true);

        System.out.println(stompClient.isRunning());

        //为了调试MyStompSessionHandler，适当地释放当前线程控制权
        while (true) {
            Object o = new Object();
            synchronized (o) {
                o.wait(1000);
            }
        }

    }
}


@Log4j2
class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("与服务器建立连接：{}", session);
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setDestination("/app/broadcast");
        stompHeaders.set("user", "测试");
        session.send(stompHeaders, "我是客户端");
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        super.handleFrame(headers, payload);
    }
}