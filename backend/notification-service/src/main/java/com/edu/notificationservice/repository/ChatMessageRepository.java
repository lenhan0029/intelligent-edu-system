package com.edu.notificationservice.repository;

import com.edu.notificationservice.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    @Query("SELECT m FROM ChatMessage m WHERE (m.senderId = :u1 AND m.recipientId = :u2) OR (m.senderId = :u2 AND m.recipientId = :u1) ORDER BY m.sentAt ASC")
    List<ChatMessage> findChatHistory(Long u1, Long u2);
}
