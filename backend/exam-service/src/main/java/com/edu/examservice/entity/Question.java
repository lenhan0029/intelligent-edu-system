package com.edu.examservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private String content;
    private String type;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;
}
