package com.pid.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralStatisticsResponseDto {

    private Long totalShows;
    private Long totalRepresentations;
    private Long totalReservations;
    private Long totalUsers;
    private BigDecimal totalRevenue;
}