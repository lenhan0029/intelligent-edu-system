package com.edu.scheduleservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attendance {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID scheduleId;
    private UUID studentId;
    private String status;
    private Date recordedAt;
}
