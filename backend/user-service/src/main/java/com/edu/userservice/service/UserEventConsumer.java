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
        log.info("Received user registered event: {}", event);

        UserProfile profile = UserProfile.builder()
                .id(Long.parseLong(event.getId()))
                .fullName(event.getFullName())
                .createdAt(new Date())
                .build();

        userProfileRepository.save(profile);
        log.info("Created user profile for user id: {}", event.getId());
    }
}
