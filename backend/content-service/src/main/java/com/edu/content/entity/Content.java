package com.edu.content.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentStatus status;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "review_comment")
    private String reviewComment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ContentType {
        LESSON, VIDEO, DOCUMENT
    }

    public enum ContentStatus {
        DRAFT, PENDING, APPROVED, REJECTED, PUBLISHED
    }
}
