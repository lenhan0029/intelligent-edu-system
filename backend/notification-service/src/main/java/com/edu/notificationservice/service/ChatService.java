package com.edu.notificationservice.service;

import com.edu.notificationservice.entity.ChatMessage;
import com.edu.notificationservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getChatHistory(Long user1Id, Long user2Id) {
        return chatMessageRepository.findChatHistory(user1Id, user2Id);
    }

    public ChatMessage sendMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }
}
