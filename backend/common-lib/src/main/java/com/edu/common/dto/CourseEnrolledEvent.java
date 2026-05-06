package com.edu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrolledEvent {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private String organizationId;
    private java.math.BigDecimal price;
}
