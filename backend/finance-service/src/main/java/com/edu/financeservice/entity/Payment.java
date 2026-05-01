package com.edu.financeservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID tuitionId;
    private BigDecimal amount;
    private String method;
    private String status;
    private Date createdAt;
}
