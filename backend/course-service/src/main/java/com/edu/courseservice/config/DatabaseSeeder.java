package com.edu.courseservice.config;

import com.edu.courseservice.entity.Course;
import com.edu.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            courseRepository.save(Course.builder()
                    .name("Advanced Java Programming")
                    .description("Master Java with deep dives into JVM, Concurrency, and Design Patterns.")
                    .status("ACTIVE")
                    .startDate(new Date())
                    .createdAt(new Date())
                    .build());

            courseRepository.save(Course.builder()
                    .name("Angular Masterclass")
                    .description("Learn to build scalable enterprise applications with Angular 17+.")
                    .status("ACTIVE")
                    .startDate(new Date())
                    .createdAt(new Date())
                    .build());

            courseRepository.save(Course.builder()
                    .name("Microservices with Spring Cloud")
                    .description("Build resilient and scalable distributed systems using Spring Boot and Spring Cloud.")
                    .status("ACTIVE")
                    .startDate(new Date())
                    .createdAt(new Date())
                    .build());
        }
    }
}
