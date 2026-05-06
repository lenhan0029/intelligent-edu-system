package com.edu.financeservice.service;

import com.edu.common.dto.CourseEnrolledEvent;
import com.edu.financeservice.entity.Tuition;
import com.edu.financeservice.repository.TuitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentEventConsumer {

    private final TuitionRepository tuitionRepository;

    @KafkaListener(topics = "course-enrolled", groupId = "finance-service-group")
    public void consumeCourseEnrolled(CourseEnrolledEvent event) {
        try {
            log.info("Received course enrolled event for student: {} in course: {}", event.getStudentId(), event.getCourseId());

            Tuition tuition = Tuition.builder()
                    .studentId(Long.parseLong(event.getStudentId()))
                    .courseId(UUID.fromString(event.getCourseId()))
                    .organizationId(event.getOrganizationId() != null ? Long.parseLong(event.getOrganizationId()) : null)
                    .amount(event.getPrice())
                    .status("UNPAID")
                    .description("Tuition for course: " + event.getCourseId())
                    .build();

            tuitionRepository.save(tuition);
            log.info("Created tuition record for enrollment: {}", event.getEnrollmentId());

        } catch (Exception e) {
            log.error("Failed to process course-enrolled event: {}", e.getMessage(), e);
        }
    }
}
