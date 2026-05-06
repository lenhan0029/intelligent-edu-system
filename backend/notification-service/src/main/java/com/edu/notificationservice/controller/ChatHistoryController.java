package com.edu.notificationservice.controller;

import com.edu.notificationservice.entity.ChatMessage;
import com.edu.notificationservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatService chatService;

    @GetMapping("/history/{userId1}/{userId2}")
    public ResponseEntity<List<ChatMessage>> getHistory(
            @PathVariable Long userId1, 
            @PathVariable Long userId2) {
        return ResponseEntity.ok(chatService.getChatHistory(userId1, userId2));
    }
}
