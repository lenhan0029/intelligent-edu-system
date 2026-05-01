package com.edu.examservice.config;

import com.edu.examservice.entity.Exam;
import com.edu.examservice.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final ExamRepository examRepository;

    @Override
    public void run(String... args) {
        if (examRepository.count() == 0) {
            examRepository.save(Exam.builder()
                    .title("Java Core Assessment")
                    .duration(90)
                    .totalScore(100)
                    .build());

            examRepository.save(Exam.builder()
                    .title("Angular UI/UX Quiz")
                    .duration(45)
                    .totalScore(50)
                    .build());

            examRepository.save(Exam.builder()
                    .title("Final Project Presentation")
                    .duration(120)
                    .totalScore(200)
                    .build());
        }
    }
}
