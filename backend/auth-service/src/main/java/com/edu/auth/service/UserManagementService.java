package com.edu.auth.service;

import com.edu.auth.entity.Role;
import com.edu.auth.entity.User;
import com.edu.auth.repository.RoleRepository;
import com.edu.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Autowired
    private org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public User createUser(String username, String email, String rawPassword, Set<String> roleNames) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .isActive(true)
                .build();

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.getRoles().add(role);
        }

        User savedUser = userRepository.save(user);

        // Sync with User Service
        com.edu.common.dto.UserRegisteredEvent event = com.edu.common.dto.UserRegisteredEvent.builder()
                .id(savedUser.getId().toString())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .fullName(username)
                .build();
        
        try {
            kafkaTemplate.send("user-registered", event);
        } catch (Exception e) {
            // Log the error but don't fail the user creation
            org.slf4j.LoggerFactory.getLogger(UserManagementService.class)
                .warn("Failed to send user-registered event to Kafka for user: {}", savedUser.getUsername());
        }

        return savedUser;
    }

    @Transactional
    public User updateUserStatus(Long userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(isActive);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserRoles(Long userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.getRoles().clear();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.getRoles().add(role);
        }
        return userRepository.save(user);
    }
}
