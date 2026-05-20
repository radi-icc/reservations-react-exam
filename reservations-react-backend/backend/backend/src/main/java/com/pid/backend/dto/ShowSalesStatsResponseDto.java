package com.pid.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSalesStatsResponseDto {

    private Long showId;
    private String showTitle;
    private Integer totalReservations;
    private Integer totalSeatsSold;
    private BigDecimal totalRevenue;
}