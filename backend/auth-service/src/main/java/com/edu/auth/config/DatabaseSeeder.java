package com.edu.auth.config;

import com.edu.auth.entity.Role;
import com.edu.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.edu.auth.entity.User;
import com.edu.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import java.util.HashSet;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String[] roles = {"ROLE_USER", "ROLE_STUDENT", "ROLE_PARENT", "ROLE_TEACHER", "ROLE_CONTENT_CREATOR", "ROLE_MODERATOR", "ROLE_FINANCE_MANAGER", "ROLE_ADMIN"};
            
            for (String roleName : roles) {
                if (!roleRepository.existsByName(roleName)) {
                    roleRepository.save(Role.builder().name(roleName).build());
                }
            }

            if (!userRepository.existsByUsername("admin")) {
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(roleRepository.findByName("ROLE_ADMIN").get());
                
                User admin = User.builder()
                        .username("admin")
                        .email("admin@smartedu.com")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .isActive(true)
                        .roles(adminRoles)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
