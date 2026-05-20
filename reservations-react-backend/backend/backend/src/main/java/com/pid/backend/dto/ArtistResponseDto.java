package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistResponseDto {

    private Long id;

    private String firstname;

    private String lastname;
}