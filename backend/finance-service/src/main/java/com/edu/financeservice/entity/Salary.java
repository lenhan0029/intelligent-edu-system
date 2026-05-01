package com.edu.financeservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
public class Salary {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID teacherId;
    private BigDecimal amount;
    private String period;
    private Date paidAt;
}
