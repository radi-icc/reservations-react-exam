package com.pid.backend.controller;

import com.pid.backend.dto.UserRequestDto;
import com.pid.backend.dto.UserResponseDto;
import com.pid.backend.dto.UserUpdateRequestDto;
import com.pid.backend.dto.ProfileUpdateRequestDto;
import com.pid.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing application users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public UserResponseDto getCurrentUserProfile() {
        return userService.getCurrentUserProfile();
    }

    @PutMapping("/me")
    public UserResponseDto updateCurrentUser(
            @Valid @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        return userService.updateCurrentUser(requestDto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
