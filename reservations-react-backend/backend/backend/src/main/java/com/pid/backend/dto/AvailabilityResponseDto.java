package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponseDto {

    private Long representationId;
    private Integer capacity;
    private Integer bookedSeats;
    private Integer availableSeats;
    private Boolean full;
}