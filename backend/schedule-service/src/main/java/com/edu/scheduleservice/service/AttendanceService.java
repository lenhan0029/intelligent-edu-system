package com.edu.scheduleservice.service;

import com.edu.common.dto.AttendanceMarkedEvent;
import com.edu.scheduleservice.entity.Attendance;
import com.edu.scheduleservice.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    @Transactional
    public Attendance markAttendance(Long studentId, UUID scheduleId, String status) {
        Attendance attendance = Attendance.builder()
                .studentId(studentId)
                .scheduleId(scheduleId)
                .status(status)
                .date(LocalDate.now())
                .build();

        Attendance saved = attendanceRepository.save(attendance);

        // Notify parent via notification-service
        AttendanceMarkedEvent event = AttendanceMarkedEvent.builder()
                .studentId(studentId.toString())
                .scheduleId(scheduleId.toString())
                .status(status)
                .date(saved.getDate().toString())
                .build();

        try {
            kafkaTemplate.send("attendance-marked", event);
        } catch (Exception e) {
            log.error("Failed to publish attendance-marked event", e);
        }

        return saved;
    }
}
