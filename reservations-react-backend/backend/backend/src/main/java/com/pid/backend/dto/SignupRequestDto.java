package com.pid.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Size(max = 60, message = "Firstname must not exceed 60 characters")
    private String firstname;

    @Size(max = 60, message = "Lastname must not exceed 60 characters")
    private String lastname;

    @Size(max = 20, message = "Language must not exceed 20 characters")
    private String language;
}