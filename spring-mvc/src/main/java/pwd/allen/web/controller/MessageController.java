package pwd.allen.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pwd.allen.entity.Fruit;

import java.util.Date;
import org.springframework.messaging.simp.annotation.support.*;

/**
 * @author 门那粒沙
 * @create 2020-02-18 9:21
 **/
@Log4j2
@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * MessageMapping：用于监听指定路径的客户端消息，处理器为 {@link SimpAnnotationMethodMessageHandler}
     * SendTo：用于将服务端的消息发送给监听了该路径的客户端
     *
     * TODO 没进来这个方法
     * @param message
     * @return
     */
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Fruit message(Message message) {
        log.info("接收到消息：{}", message.getPayload());
        return new Fruit("hello!" + message.getPayload(), 0f, new Date());
    }

    @GetMapping(value = "/message/{message}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object sendMessage(@PathVariable String message) {
        this.template.convertAndSend("/topic/control", message);
        return "send：" + message;
    }
}
