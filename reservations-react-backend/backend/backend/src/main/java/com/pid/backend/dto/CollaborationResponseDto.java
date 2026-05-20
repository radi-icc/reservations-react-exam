package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationResponseDto {

    private Long id;

    private Long showId;
    private String showTitle;

    private Long artistId;
    private String artistFirstname;
    private String artistLastname;

    private Long artistTypeId;
    private String artistTypeName;
}