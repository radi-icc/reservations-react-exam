package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistTypeResponseDto {

    private Long id;
    private String typeName;
}