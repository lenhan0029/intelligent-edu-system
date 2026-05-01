package com.edu.auth.config;

import com.edu.auth.entity.Role;
import com.edu.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName("ROLE_USER")) {
                roleRepository.save(Role.builder().name("ROLE_USER").build());
            }
            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            }
        };
    }
}
