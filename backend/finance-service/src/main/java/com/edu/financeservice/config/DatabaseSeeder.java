package com.edu.financeservice.config;

import com.edu.financeservice.entity.Tuition;
import com.edu.financeservice.repository.TuitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final TuitionRepository tuitionRepository;

    @Override
    public void run(String... args) {
        if (tuitionRepository.count() == 0) {
            tuitionRepository.save(Tuition.builder()
                    .description("Spring Semester Tuition 2024")
                    .amount(new BigDecimal("1200.00"))
                    .status("UNPAID")
                    .dueDate(new Date())
                    .build());

            tuitionRepository.save(Tuition.builder()
                    .description("Lab Materials Fee - Computer Science")
                    .amount(new BigDecimal("150.00"))
                    .status("UNPAID")
                    .dueDate(new Date())
                    .build());

            tuitionRepository.save(Tuition.builder()
                    .description("Library Membership Renewal")
                    .amount(new BigDecimal("50.00"))
                    .status("UNPAID")
                    .dueDate(new Date())
                    .build());
        }
    }
}
