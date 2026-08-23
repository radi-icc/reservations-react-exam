package com.pid.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;

    private Long userId;
    private String username;

    private Long showId;
    private String showTitle;

    private Integer rating;
    private String comment;
    private String reviewType;
    private String sourceUrl;
    private Boolean published;
    private LocalDateTime createdAt;
}
