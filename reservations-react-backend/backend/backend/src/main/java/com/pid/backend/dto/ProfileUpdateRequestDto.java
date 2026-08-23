package com.pid.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequestDto {

    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 60, message = "Firstname must not exceed 60 characters")
    private String firstname;

    @Size(max = 60, message = "Lastname must not exceed 60 characters")
    private String lastname;

    @Size(max = 20, message = "Language must not exceed 20 characters")
    private String language;
}
