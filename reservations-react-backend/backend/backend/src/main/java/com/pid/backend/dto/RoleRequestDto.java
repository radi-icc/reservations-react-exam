package com.pid.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDto {

    @NotBlank(message = "Role name is required")
    private String roleName;
}