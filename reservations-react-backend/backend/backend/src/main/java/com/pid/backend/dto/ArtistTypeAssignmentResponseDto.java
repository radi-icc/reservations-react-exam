package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistTypeAssignmentResponseDto {

    private Long id;

    private Long artistId;
    private String artistFirstname;
    private String artistLastname;

    private Long artistTypeId;
    private String artistTypeName;
}