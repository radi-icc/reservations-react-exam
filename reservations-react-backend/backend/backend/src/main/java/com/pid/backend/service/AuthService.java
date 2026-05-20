package com.pid.backend.service;

import com.pid.backend.dto.*;
import com.pid.backend.entity.Role;
import com.pid.backend.entity.User;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.RoleRepository;
import com.pid.backend.repository.UserRepository;
import com.pid.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles signup and login.
 *
 * Signup creates a MEMBER user by default.
 * Login validates credentials and returns a JWT token.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE = "MEMBER";
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponseDto signup(SignupRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match");
        }

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new BadRequestException("Username already exists: " + requestDto.getUsername());
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BadRequestException("Email already exists: " + requestDto.getEmail());
        }

        Role memberRole = roleRepository.findByRoleName(DEFAULT_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException("Default role MEMBER not found"));

        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .firstname(requestDto.getFirstname())
                .lastname(requestDto.getLastname())
                .language(requestDto.getLanguage())
                .enabled(true)
                .role(memberRole)
                .build();

        User savedUser = userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getUsername())
                .password(savedUser.getPassword())
                .authorities("ROLE_" + savedUser.getRole().getRoleName())
                .build();

        String token = jwtService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roleName(savedUser.getRole().getRoleName())
                .build();
    }

    public AuthResponseDto login(LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsernameOrEmail(),
                        requestDto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsernameOrEmail(
                        requestDto.getUsernameOrEmail(),
                        requestDto.getUsernameOrEmail()
                )
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .build();
    }

    public AuthMeResponseDto getCurrentUserProfile() {
        User user = currentUserService.getCurrentUser();

        return AuthMeResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .language(user.getLanguage())
                .enabled(user.getEnabled())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .build();
    }
}