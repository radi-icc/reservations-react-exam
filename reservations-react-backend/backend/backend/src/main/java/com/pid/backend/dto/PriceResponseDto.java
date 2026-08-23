package com.pid.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceResponseDto {

    private Long id;
    private String label;
    private BigDecimal amount;
    private Long representationId;
    private String showTitle;
    private String performanceDate;
    private String performanceTime;
}
