package com.pid.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationRequestDto {

    @NotNull(message = "Artist type assignment id is required")
    private Long artistTypeAssignmentId;

    @NotNull(message = "Show id is required")
    private Long showId;
}