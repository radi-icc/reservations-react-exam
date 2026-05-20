package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMeResponseDto {

    private Long userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String language;
    private Boolean enabled;
    private Long roleId;
    private String roleName;
}