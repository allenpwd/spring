package pwd.allen.web.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 自定义{@link org.springframework.messaging.support.ChannelInterceptor}，实现断开连接的处理
 *
 * @author 门那粒沙
 * @create 2020-02-18 19:57
 */
@Component
@Log4j2
public class MyChannelInterceptor extends ChannelInterceptorAdapter {

    /**
     * 记录在线用户
     */
    private CopyOnWriteArraySet<String> set_users = new CopyOnWriteArraySet<>();

//    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        Principal user1 = SimpMessageHeaderAccessor.getUser(message.getHeaders());

        String user = null;
        try {
            user = accessor.getNativeHeader("user").get(0);
        } catch (Exception e) {}
        if (StringUtils.isEmpty(user)) {
            Principal principal = accessor.getUser();
            if (principal != null && !StringUtils.isEmpty(principal.getName())) {
                user = principal.getName();
            } else {
                user = accessor.getSessionId();
            }
        }
        accessor.setNativeHeader("user", user);
        if (StompCommand.CONNECTED.equals(command)) {
            set_users.add(user);
            sendUsers();
        } else if (StompCommand.DISCONNECT.equals(command)) {//用户已经断开连接
            set_users.remove(user);
            sendUsers();
            log.debug(MessageFormat.format("用户{0}的WebSocket连接已经断开", user));
        }
    }

    public void sendUsers() {
//        template.convertAndSend("/user/list", set_users);
    }

}
