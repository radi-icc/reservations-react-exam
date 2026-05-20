package com.pid.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistTypeAssignmentRequestDto {

    @NotNull(message = "Artist id is required")
    private Long artistId;

    @NotNull(message = "Artist type id is required")
    private Long artistTypeId;
}