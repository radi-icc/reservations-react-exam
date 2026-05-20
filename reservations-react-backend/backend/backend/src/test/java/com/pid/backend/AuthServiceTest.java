package com.pid.backend;

import com.pid.backend.dto.SignupRequestDto;
import com.pid.backend.entity.Role;
import com.pid.backend.entity.User;
import com.pid.backend.repository.RoleRepository;
import com.pid.backend.repository.UserRepository;
import com.pid.backend.security.JwtService;
import com.pid.backend.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private AuthService authService;

    @Test
    void signupShouldCreateUserAndReturnToken() {
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .username("john")
                .email("john@example.com")
                .password("Password123!")
                .confirmPassword("Password123!")
                .firstname("John")
                .lastname("Doe")
                .language("en")
                .build();

        Role role = Role.builder()
                .id(1L)
                .roleName("MEMBER")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("encoded-password")
                .role(role)
                .enabled(true)
                .build();

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findByRoleName("MEMBER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Password123!")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        var response = authService.signup(requestDto);

        assertEquals("jwt-token", response.getToken());
        assertEquals("john", response.getUsername());
        assertEquals("MEMBER", response.getRoleName());

        verify(userRepository).save(any(User.class));
    }
}