package com.edu.examservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "organization_id")
    private Long organizationId;

    private String title;
    private Integer duration;
    
    @Column(name = "total_score")
    private Integer totalScore;

    @OneToMany(mappedBy = "exam")
    private List<Question> questions;
}
