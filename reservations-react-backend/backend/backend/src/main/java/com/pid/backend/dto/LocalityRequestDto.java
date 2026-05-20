package com.pid.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalityRequestDto {

    @NotBlank(message = "Postal code is required")
    @Size(max = 10)
    private String postalCode;

    @NotBlank(message = "Locality is required")
    @Size(max = 60)
    private String locality;
}