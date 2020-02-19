package pwd.allen.web.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.text.MessageFormat;

/**
 * 自定义{@link org.springframework.messaging.support.ChannelInterceptor}，实现断开连接的处理
 * @author 门那粒沙
 * @create 2020-02-18 19:57
 */
@Component
@Log4j2
public class MyChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        //用户已经断开连接
        if(StompCommand.DISCONNECT.equals(command)){
            String user = "";
            Principal principal = accessor.getUser();
            if(principal != null && !StringUtils.isEmpty(principal.getName())){
                user = principal.getName();
            }else{
                user = accessor.getSessionId();
            }

            log.debug(MessageFormat.format("用户{0}的WebSocket连接已经断开", user));
        }
    }
}
