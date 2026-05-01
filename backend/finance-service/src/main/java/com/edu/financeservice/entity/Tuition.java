package com.edu.financeservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tuition {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private String description;
    private BigDecimal amount;
    private String status;
    private Date dueDate;
    private Date paidAt;
}
