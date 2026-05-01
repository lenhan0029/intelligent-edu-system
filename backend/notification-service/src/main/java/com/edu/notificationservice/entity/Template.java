package com.edu.notificationservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
public class Template {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String content;
}
