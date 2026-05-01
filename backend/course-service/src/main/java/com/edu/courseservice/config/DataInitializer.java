package com.edu.courseservice.config;

import com.edu.courseservice.entity.Course;
import com.edu.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Date;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CourseRepository courseRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (courseRepository.count() == 0) {
                courseRepository.save(Course.builder()
                        .name("Introduction to Microservices")
                        .description("Learn the basics of building scalable systems with Spring Boot and Cloud.")
                        .teacherId(UUID.randomUUID())
                        .status("Active")
                        .createdAt(new Date())
                        .build());

                courseRepository.save(Course.builder()
                        .name("Advanced Angular Development")
                        .description("Master standalone components, signals, and advanced routing in Angular.")
                        .teacherId(UUID.randomUUID())
                        .status("Coming Soon")
                        .createdAt(new Date())
                        .build());
                
                courseRepository.save(Course.builder()
                        .name("Database Design & Optimization")
                        .description("Deep dive into PostgreSQL, indexing, and high-performance queries.")
                        .teacherId(UUID.randomUUID())
                        .status("Active")
                        .createdAt(new Date())
                        .build());
            }
        };
    }
}
