package com.edu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceMarkedEvent {
    private String studentId;
    private String scheduleId;
    private String status;
    private String date;
}
