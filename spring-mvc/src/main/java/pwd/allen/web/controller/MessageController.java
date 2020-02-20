package pwd.allen.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.support.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 门那粒沙
 * @create 2020-02-18 9:21
 **/
@Log4j2
@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * MessageMapping：用于监听指定路径的客户端消息，处理器为 {@link SimpAnnotationMethodMessageHandler}
     * SendTo：用于将服务端的消息发送给监听了该路径的客户端，处理器为 {@link SendToMethodReturnValueHandler}
     *
     * 判断mapping的代码：{@link org.springframework.messaging.handler.invocation.AbstractMethodMessageHandler#handleMessageInternal(Message, String)}
     *
     * @param message
     * @return
     */
    @MessageMapping("/broadcast")
    @SendTo("/topic/message")
    public Object message(String message, SimpMessageHeaderAccessor headerAccessor, @Header("user") String user) {
        log.info("headerAccessor：{}，接收到消息：{}", headerAccessor, message);
        return String.format("【%s】%s", user, message);
    }

    /**
     * 使用 SimpMessagingTemplate 服务端主动向客户端发送信息
     * @param message
     * @return
     */
    @GetMapping(value = "/message/{message}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object sendMessage(@PathVariable String message, @RequestParam(required = false) String destination) {
        if (destination == null) destination = "/topic/message";
        this.template.convertAndSend(destination, message);
        return "send：" + message;
    }

    @MessageMapping("/chat")
    public void chat(String message, @Header("user") String user, @Header("target") String target) {
        log.info("user：{},target：{},接收到消息：{}", user, target, message);
        this.template.convertAndSend("/topic/" + target, message);
    }

    /**
     * 使用@SendToUser返回消息
     * TODO 没有效果 可能需要加上登录认证的东西
     * @see org.springframework.messaging.simp.user.UserDestinationMessageHandler
     *
     * @param message
     * @param user
     * @param headers
     * @return
     */
    @MessageMapping("/resp")
    @SendToUser("/resp")//客户端需要订阅/user/resp
    public String  resp(String message, @Header("user") String user, @Headers Map headers) {
        log.info("headers：{},接收到消息：{}", headers, message);
        return String.format("【%s】%s", user, message);
    }
}
