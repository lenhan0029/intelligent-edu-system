package com.edu.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;
    private String content;
    
    @Column(name = "video_url")
    private String videoUrl;
    
    @Column(name = "order_index")
    private Integer orderIndex;

    @OneToMany(mappedBy = "lesson")
    private List<Material> materials;
}
