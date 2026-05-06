package com.edu.examservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FraudDetectionService {

    public boolean detectFraud(UUID studentId, UUID examId, String submissionData) {
        log.info("Performing AI-based fraud detection for student: {} in exam: {}", studentId, examId);
        
        // Basic check: if submission is suspiciously short/fast
        // In real system, this would analyze browser behavior, copy-paste events, etc.
        if (submissionData != null && submissionData.length() < 10) {
            log.warn("Suspiciously short submission detected for student: {}", studentId);
            return true;
        }
        
        return false;
    }
}
