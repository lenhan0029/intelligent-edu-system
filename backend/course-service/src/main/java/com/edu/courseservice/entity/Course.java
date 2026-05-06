package com.edu.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    
    @Column(name = "teacher_id")
    private UUID teacherId;

    @Column(name = "organization_id")
    private Long organizationId;
    
    private String status;
    private java.math.BigDecimal price;
    
    @Column(name = "start_date")
    private Date startDate;
    
    @Column(name = "end_date")
    private Date endDate;
    
    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;
}
