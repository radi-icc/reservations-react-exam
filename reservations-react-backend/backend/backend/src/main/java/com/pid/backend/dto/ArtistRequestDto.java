package com.pid.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistRequestDto {

    @NotBlank(message = "Firstname is required")
    @Size(max = 60, message = "Firstname must not exceed 60 characters")
    private String firstname;

    @NotBlank(message = "Lastname is required")
    @Size(max = 60, message = "Lastname must not exceed 60 characters")
    private String lastname;
}