package pwd.allen.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.Map;

/**
 * @author 门那粒沙
 * @create 2020-02-18 9:21
 **/
@Log4j2
@Controller
public class MessageController {

    @MessageMapping("/control")
    @SendTo("/topic/control")
    public Map message(Message message) {
        log.info("接收到消息：{}", message.getPayload());
        return Collections.singletonMap("msg", "hello!" + message.getPayload());
    }
}
