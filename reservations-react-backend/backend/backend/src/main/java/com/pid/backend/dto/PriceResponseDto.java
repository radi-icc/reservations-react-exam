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
}