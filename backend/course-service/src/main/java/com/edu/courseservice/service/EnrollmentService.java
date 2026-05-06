package com.edu.courseservice.service;

import com.edu.common.dto.CourseEnrolledEvent;
import com.edu.courseservice.entity.Course;
import com.edu.courseservice.entity.Enrollment;
import com.edu.courseservice.repository.CourseRepository;
import com.edu.courseservice.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Enrollment enrollStudent(Long studentId, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .course(course)
                .enrollmentDate(new Date())
                .status("PENDING_PAYMENT")
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Publish Event
        CourseEnrolledEvent event = CourseEnrolledEvent.builder()
                .enrollmentId(savedEnrollment.getId().toString())
                .studentId(studentId.toString())
                .courseId(courseId.toString())
                .organizationId(course.getOrganizationId() != null ? course.getOrganizationId().toString() : null)
                .price(course.getPrice() != null ? course.getPrice() : java.math.BigDecimal.ZERO)
                .build();

        try {
            kafkaTemplate.send("course-enrolled", event);
        } catch (Exception e) {
            log.error("Failed to publish course-enrolled event", e);
        }

        return savedEnrollment;
    }
}
