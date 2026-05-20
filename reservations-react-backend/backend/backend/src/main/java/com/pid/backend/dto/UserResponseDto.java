package com.pid.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String language;
    private Boolean enabled;
    private LocalDateTime createdAt;

    private Long roleId;
    private String roleName;
}