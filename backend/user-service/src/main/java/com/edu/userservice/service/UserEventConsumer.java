package com.edu.userservice.service;

import com.edu.common.dto.UserRegisteredEvent;
import com.edu.userservice.entity.Role;
import com.edu.userservice.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

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

            Long organizationId = null;
            if (event.getOrganizationId() != null && !event.getOrganizationId().isEmpty()) {
                try {
                    organizationId = Long.parseLong(event.getOrganizationId());
                } catch (NumberFormatException e) {
                    log.warn("Invalid organizationId format: {}", event.getOrganizationId());
                }
            }

            UserProfile profile = UserProfile.builder()
                    .id(userId)
                    .fullName(event.getFullName())
                    .organizationId(organizationId)
                    .createdAt(new Date())
                    .build();

            if (event.getRoles() != null && !event.getRoles().isEmpty()) {
                java.util.Set<Role> roles = new java.util.HashSet<>();
                for (String roleName : event.getRoles()) {
                    roleRepository.findByName(roleName).ifPresentOrElse(
                        roles::add,
                        () -> log.warn("Role not found in user-service: {}", roleName)
                    );
                }
                profile.setRoles(roles);
            }

            userProfileRepository.save(profile);
            log.info("Successfully created/updated user profile for userId: {}", userId);

        } catch (Exception e) {
            // Log the error but do NOT rethrow - prevents Kafka from retrying indefinitely
            // which would cause high CPU usage on Kafka broker
            log.error("Failed to process user-registered event for userId: {}. Error: {}",
                    event != null ? event.getId() : "null", e.getMessage(), e);
        }
    }
}
