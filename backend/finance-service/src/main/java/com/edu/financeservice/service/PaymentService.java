package com.edu.financeservice.service;

import com.edu.common.dto.PaymentStatusUpdatedEvent;
import com.edu.financeservice.entity.Payment;
import com.edu.financeservice.entity.Tuition;
import com.edu.financeservice.repository.PaymentRepository;
import com.edu.financeservice.repository.TuitionRepository;
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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TuitionRepository tuitionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Payment processPayment(UUID tuitionId, String method) {
        Tuition tuition = tuitionRepository.findById(tuitionId)
                .orElseThrow(() -> new RuntimeException("Tuition not found"));

        if ("PAID".equals(tuition.getStatus())) {
            throw new RuntimeException("Tuition already paid");
        }

        // Mock payment gateway integration
        log.info("Processing payment for tuition: {} via {}", tuitionId, method);
        
        Payment payment = Payment.builder()
                .tuitionId(tuitionId)
                .amount(tuition.getAmount())
                .method(method)
                .status("SUCCESS")
                .createdAt(new Date())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update tuition status
        tuition.setStatus("PAID");
        tuition.setPaidAt(new Date());
        tuitionRepository.save(tuition);

        // Notify other services
        PaymentStatusUpdatedEvent event = PaymentStatusUpdatedEvent.builder()
                .paymentId(savedPayment.getId().toString())
                .tuitionId(tuitionId.toString())
                .status("SUCCESS")
                .studentId(tuition.getStudentId().toString())
                .build();

        try {
            kafkaTemplate.send("payment-status-updated", event);
        } catch (Exception e) {
            log.error("Failed to publish payment-status-updated event", e);
        }

        return savedPayment;
    }
}
