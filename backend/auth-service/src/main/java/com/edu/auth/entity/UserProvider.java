package com.edu.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_providers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider_name", "provider_id"})
})
public class UserProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "provider_name", nullable = false)
    private String providerName; // e.g. "google", "github"

    @Column(name = "provider_id", nullable = false)
    private String providerId;
}
