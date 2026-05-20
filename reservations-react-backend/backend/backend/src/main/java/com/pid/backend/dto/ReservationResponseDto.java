package com.pid.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDto {

    private Long reservationId;

    private Long userId;
    private String username;
    private String email;

    private Long representationId;
    private String showTitle;
    private LocalDate performanceDate;
    private LocalTime performanceTime;

    private Long priceId;
    private String priceLabel;
    private BigDecimal unitPrice;

    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime reservationDate;
}