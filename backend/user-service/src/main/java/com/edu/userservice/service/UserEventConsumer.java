package com.edu.userservice.service;

import com.edu.common.dto.UserRegisteredEvent;
import com.edu.userservice.entity.UserProfile;
import com.edu.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final UserProfileRepository userProfileRepository;

    @KafkaListener(topics = "user-registered", groupId = "user-service-group")
    public void consumeUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            log.info("Received user registered event for userId: {}", event.getId());

            Long userId = Long.parseLong(event.getId());

            // Skip if profile already exists to prevent duplicate constraint errors
            if (userProfileRepository.existsById(userId)) {
                log.warn("UserProfile already exists for userId: {}, skipping.", userId);
                return;
            }

            UserProfile profile = UserProfile.builder()
                    .id(userId)
                    .fullName(event.getFullName())
                    .createdAt(new Date())
                    .build();

            userProfileRepository.save(profile);
            log.info("Created user profile for userId: {}", userId);

        } catch (Exception e) {
            // Log the error but do NOT rethrow - prevents Kafka from retrying indefinitely
            // which would cause high CPU usage on Kafka broker
            log.error("Failed to process user-registered event for userId: {}. Error: {}",
                    event != null ? event.getId() : "null", e.getMessage(), e);
        }
    }
}
