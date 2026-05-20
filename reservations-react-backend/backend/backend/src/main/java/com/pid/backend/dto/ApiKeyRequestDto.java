package com.pid.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKeyRequestDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Affiliate plan id is required")
    private Long affiliatePlanId;
}