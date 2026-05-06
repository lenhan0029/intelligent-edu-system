package com.edu.courseservice.service;

import com.edu.common.dto.PaymentStatusUpdatedEvent;
import com.edu.courseservice.entity.Enrollment;
import com.edu.courseservice.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final EnrollmentRepository enrollmentRepository;

    @KafkaListener(topics = "payment-status-updated", groupId = "course-service-group")
    @Transactional
    public void consumePaymentStatusUpdated(PaymentStatusUpdatedEvent event) {
        log.info("Received payment status update for tuition: {} - status: {}", event.getTuitionId(), event.getStatus());

        if ("SUCCESS".equals(event.getStatus())) {
            UUID enrollmentId = UUID.fromString(event.getTuitionId()); // We used TuitionId = EnrollmentId in this design logic
            enrollmentRepository.findById(enrollmentId).ifPresent(enrollment -> {
                enrollment.setStatus("ACTIVE");
                enrollmentRepository.save(enrollment);
                log.info("Enrollment {} is now ACTIVE", enrollmentId);
            });
        }
    }
}
