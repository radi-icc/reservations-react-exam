package com.pid.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowResponseDto {

    private Long id;
    private Long locationId;
    private String locationDesignation;

    private Long producerId;
    private String producerName;
    private String slug;
    private String title;
    private String posterUrl;
    private Boolean bookable;
    private BigDecimal price;
    private String description;
    private LocalDateTime createdAt;
}
