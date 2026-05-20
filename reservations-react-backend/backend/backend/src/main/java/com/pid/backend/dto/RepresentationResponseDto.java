package com.pid.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepresentationResponseDto {

    private Long id;
    private Long showId;
    private String showTitle;
    private Long locationId;
    private String locationDesignation;
    private LocalDate performanceDate;
    private LocalTime performanceTime;
    private Integer capacity;
    private Integer bookedSeats;
    private Boolean full;
}