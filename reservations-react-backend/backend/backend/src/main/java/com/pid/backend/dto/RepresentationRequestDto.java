package com.pid.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepresentationRequestDto {

    @NotNull(message = "Show id is required")
    private Long showId;

    @NotNull(message = "Location id is required")
    private Long locationId;

    @NotNull(message = "Performance date is required")
    private LocalDate performanceDate;

    @NotNull(message = "Performance time is required")
    private LocalTime performanceTime;

    @Min(value = 0, message = "Capacity must be zero or greater")
    private Integer capacity;

    @Min(value = 0, message = "Booked seats must be zero or greater")
    private Integer bookedSeats;
}