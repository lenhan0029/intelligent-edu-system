package com.edu.auth.service;

import com.edu.common.dto.OrganizationCreatedEvent;
import com.edu.auth.entity.Role;
import com.edu.auth.entity.User;
import com.edu.auth.repository.RoleRepository;
import com.edu.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrgEventConsumer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @KafkaListener(topics = "organization-created", groupId = "auth-service-group")
    public void consumeOrganizationCreated(OrganizationCreatedEvent event) {
        try {
            log.info("Received organization created event: {}", event.getName());

            // Check if user with adminEmail already exists
            userRepository.findByEmail(event.getAdminEmail()).ifPresentOrElse(
                user -> {
                    log.info("User {} already exists, assigning as ADMIN for organization {}", user.getEmail(), event.getId());
                    user.setOrganizationId(Long.parseLong(event.getId()));
                    assignRole(user, "ROLE_ADMIN");
                    userRepository.save(user);
                },
                () -> {
                    log.info("Creating new ADMIN user for organization: {}", event.getName());
                    User admin = User.builder()
                            .username(event.getAdminEmail()) // Use email as username for now
                            .email(event.getAdminEmail())
                            .passwordHash(passwordEncoder.encode("admin123")) // Default password, should be changed
                            .organizationId(Long.parseLong(event.getId()))
                            .isActive(true)
                            .build();
                    
                    assignRole(admin, "ROLE_ADMIN");
                    userRepository.save(admin);
                }
            );

        } catch (Exception e) {
            log.error("Failed to process organization-created event: {}", e.getMessage(), e);
        }
    }

    private void assignRole(User user, String roleName) {
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        roleRepository.findByName(roleName).ifPresent(role -> user.getRoles().add(role));
    }
}
