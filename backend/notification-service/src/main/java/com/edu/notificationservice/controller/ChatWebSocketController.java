package com.edu.notificationservice.controller;

import com.edu.notificationservice.entity.ChatMessage;
import com.edu.notificationservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        // Save to DB
        ChatMessage saved = chatService.sendMessage(chatMessage);
        
        // Send to recipient
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(), 
                "/queue/messages", 
                saved
        );
    }
}
