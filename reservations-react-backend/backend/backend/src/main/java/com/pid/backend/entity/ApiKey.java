package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", unique = true)
    private String apiKey;

    private Boolean enabled;

    @Column(name = "api_usage_count")
    private Integer apiUsageCount;

    @Column(name = "api_usage_period_start")
    private LocalDateTime apiUsagePeriodStart;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "affiliate_plan_id", nullable = false)
    private AffiliatePlan affiliatePlan;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (enabled == null) {
            enabled = true;
        }

        if (apiUsageCount == null) {
            apiUsageCount = 0;
        }

        if (apiUsagePeriodStart == null) {
            apiUsagePeriodStart = LocalDateTime.now();
        }
    }
}
