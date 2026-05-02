package com.edu.auth.service;

import com.edu.auth.entity.Role;
import com.edu.auth.entity.User;
import com.edu.auth.repository.RoleRepository;
import com.edu.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean canManageUser(User targetUser) {
        String currentUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUserUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Set<String> currentUserRoles = currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        Set<String> targetUserRoles = targetUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        if (currentUserRoles.contains("ROLE_SUPERADMIN")) {
            return true; // Superadmin can manage everyone
        }

        if (currentUserRoles.contains("ROLE_ADMIN")) {
            // Admin cannot manage other Admins or Superadmins
            return !targetUserRoles.contains("ROLE_ADMIN") && !targetUserRoles.contains("ROLE_SUPERADMIN");
        }

        return false;
    }

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

        // Check permission to create user with these roles
        if (!canManageUser(user)) {
            throw new RuntimeException("You don't have permission to create a user with these roles");
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
            org.slf4j.LoggerFactory.getLogger(UserManagementService.class)
                .warn("Failed to send user-registered event to Kafka for user: {}", savedUser.getUsername());
        }

        return savedUser;
    }

    @Transactional
    public User updateUserStatus(Long userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!canManageUser(user)) {
            throw new RuntimeException("You don't have permission to modify this user");
        }
        
        user.setActive(isActive);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserRoles(Long userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!canManageUser(user)) {
            throw new RuntimeException("You don't have permission to modify this user's roles");
        }

        user.getRoles().clear();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.getRoles().add(role);
        }

        // Check if the NEW roles are also within current user's management range
        if (!canManageUser(user)) {
            throw new RuntimeException("You don't have permission to assign these roles");
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!canManageUser(user)) {
            throw new RuntimeException("You don't have permission to delete this user");
        }
        
        userRepository.delete(user);
    }
}
