package com.pid.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliatePlanRequestDto {

    @NotBlank(message = "Plan name is required")
    private String planName;

    @Min(value = 0, message = "API limit must be zero or greater")
    private Integer apiLimit;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal monthlyPrice;
}