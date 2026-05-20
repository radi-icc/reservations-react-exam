package com.pid.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistTypeRequestDto {

    @NotBlank(message = "Type name is required")
    @Size(max = 60)
    private String typeName;
}