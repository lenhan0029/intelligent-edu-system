package com.edu.content.service;

import com.edu.content.entity.Content;
import com.edu.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {

    private final ContentRepository contentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<Content> getOrganizationContent(Long orgId) {
        return contentRepository.findByOrganizationId(orgId);
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found"));
    }

    @Transactional
    public Content createContent(Content content) {
        content.setStatus(Content.ContentStatus.DRAFT);
        return contentRepository.save(content);
    }

    @Transactional
    public Content submitForReview(Long id) {
        Content content = getContentById(id);
        if (content.getStatus() != Content.ContentStatus.DRAFT && content.getStatus() != Content.ContentStatus.REJECTED) {
            throw new RuntimeException("Content must be in DRAFT or REJECTED state to submit for review");
        }
        content.setStatus(Content.ContentStatus.PENDING);
        return contentRepository.save(content);
    }

    @Transactional
    public Content approveContent(Long id, Long reviewerId) {
        Content content = getContentById(id);
        if (content.getStatus() != Content.ContentStatus.PENDING) {
            throw new RuntimeException("Content must be in PENDING state to approve");
        }
        content.setStatus(Content.ContentStatus.APPROVED);
        content.setReviewerId(reviewerId);
        
        Content savedContent = contentRepository.save(content);
        
        // Notify or publish event
        publishContentApprovedEvent(savedContent);
        
        return savedContent;
    }

    @Transactional
    public Content rejectContent(Long id, Long reviewerId, String comment) {
        Content content = getContentById(id);
        if (content.getStatus() != Content.ContentStatus.PENDING) {
            throw new RuntimeException("Content must be in PENDING state to reject");
        }
        content.setStatus(Content.ContentStatus.REJECTED);
        content.setReviewerId(reviewerId);
        content.setReviewComment(comment);
        return contentRepository.save(content);
    }

    @Transactional
    public Content publishContent(Long id) {
        Content content = getContentById(id);
        if (content.getStatus() != Content.ContentStatus.APPROVED) {
            throw new RuntimeException("Content must be APPROVED before publishing");
        }
        content.setStatus(Content.ContentStatus.PUBLISHED);
        return contentRepository.save(content);
    }

    private void publishContentApprovedEvent(Content content) {
        try {
            // Simplified event for now
            kafkaTemplate.send("content-approved", content.getId().toString());
        } catch (Exception e) {
            log.error("Failed to publish content-approved event", e);
        }
    }
}
