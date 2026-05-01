package com.edu.fileservice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FileMetadata {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private UUID uploaderId;
    private Date uploadedAt;
}
