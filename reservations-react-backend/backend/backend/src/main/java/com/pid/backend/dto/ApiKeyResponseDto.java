package com.pid.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKeyResponseDto {

    private Long id;

    private Long userId;
    private String username;
    private String email;

    private Long affiliatePlanId;
    private String affiliatePlanName;

    private String apiKey;
    private Boolean enabled;
    private Integer apiLimit;
    private Integer apiUsageCount;
    private Integer remainingCalls;
    private LocalDateTime createdAt;
}
