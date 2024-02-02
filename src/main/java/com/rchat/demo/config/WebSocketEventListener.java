package com.rchat.demo.config;

import com.rchat.demo.chat.ChatMessage;
import com.rchat.demo.chat.MessagType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisConnectListener(SessionDisconnectEvent event)
    {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
        if(username != null)
        {
            log.info("user disconnected: {}",username);
            var chatMessage = ChatMessage.builder()
                    .type(MessagType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
