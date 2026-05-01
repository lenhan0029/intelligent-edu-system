package com.edu.scheduleservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID courseId;
    private UUID teacherId;
    private String room;
    private Date startTime;
    private Date endTime;
}
