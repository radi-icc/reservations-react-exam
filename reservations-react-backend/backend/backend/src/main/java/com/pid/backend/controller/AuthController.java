package com.pid.backend.controller;

import com.pid.backend.dto.*;
import com.pid.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Public authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return authService.signup(requestDto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authService.login(requestDto);
    }
    @GetMapping("/me")
    public AuthMeResponseDto getCurrentUserProfile() {
        return authService.getCurrentUserProfile();
    }
}