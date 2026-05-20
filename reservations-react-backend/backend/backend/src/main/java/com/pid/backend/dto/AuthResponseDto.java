package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String token;
    private String tokenType;

    private Long userId;
    private String username;
    private String email;
    private String roleName;
}