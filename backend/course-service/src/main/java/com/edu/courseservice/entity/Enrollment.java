package com.edu.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "student_id")
    private UUID studentId;

    @Column(name = "course_id")
    private UUID courseId;

    private String status;
    
    @Column(name = "enrolled_at")
    private Date enrolledAt;
}
