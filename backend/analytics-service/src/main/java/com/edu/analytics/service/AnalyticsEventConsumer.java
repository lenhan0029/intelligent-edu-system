package com.edu.analytics.service;

import com.edu.analytics.entity.AnalyticsRecord;
import com.edu.analytics.repository.AnalyticsRepository;
import com.edu.common.dto.CourseEnrolledEvent;
import com.edu.common.dto.AttendanceMarkedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventConsumer {

    private final AnalyticsRepository analyticsRepository;

    @KafkaListener(topics = "course-enrolled", groupId = "analytics-group")
    public void consumeCourseEnrolled(CourseEnrolledEvent event) {
        log.info("Recording enrollment analytics for student: {}", event.getStudentId());
        saveRecord(event.getStudentId(), "COURSE_ENROLLMENT", event.toString(), event.getOrganizationId());
    }

    @KafkaListener(topics = "attendance-marked", groupId = "analytics-group")
    public void consumeAttendance(AttendanceMarkedEvent event) {
        log.info("Recording attendance analytics for student: {}", event.getStudentId());
        saveRecord(event.getStudentId(), "ATTENDANCE", event.toString(), null); // Org ID might need to be added to event
    }

    private void saveRecord(String studentId, String type, String data, String orgId) {
        AnalyticsRecord record = AnalyticsRecord.builder()
                .studentId(studentId)
                .eventType(type)
                .data(data)
                .organizationId(orgId != null ? Long.parseLong(orgId) : null)
                .build();
        analyticsRepository.save(record);
    }
}
