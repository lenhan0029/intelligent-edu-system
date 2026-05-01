package com.edu.notificationservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private String type;
    private String content;
    private String status;
    private Date createdAt;
}
