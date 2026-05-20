package com.pid.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliatePlanResponseDto {

    private Long id;
    private String planName;
    private Integer apiLimit;
    private BigDecimal monthlyPrice;
}