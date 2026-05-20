package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalityResponseDto {

    private Long id;
    private String postalCode;
    private String locality;
}