package com.pid.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowRequestDto {

    @NotNull(message = "Location id is required")
    private Long locationId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Poster url must not exceed 255 characters")
    private String posterUrl;

    private Boolean bookable;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or greater")
    private BigDecimal price;

    private String description;
}