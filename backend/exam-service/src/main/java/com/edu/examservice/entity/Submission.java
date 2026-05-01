package com.edu.examservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "exam_id")
    private UUID examId;

    @Column(name = "student_id")
    private UUID studentId;

    private Float score;
    
    @Column(name = "submitted_at")
    private Date submittedAt;
}
