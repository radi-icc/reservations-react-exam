package com.pid.backend.service;

import com.pid.backend.dto.UserRequestDto;
import com.pid.backend.dto.UserResponseDto;
import com.pid.backend.dto.UserUpdateRequestDto;
import com.pid.backend.entity.Role;
import com.pid.backend.entity.User;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.RoleRepository;
import com.pid.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for user management.
 *
 * This creates members, admins, producers, critics and affiliates.
 * Passwords are stored using BCrypt instead of plain text.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapToResponse(user);
    }

    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new BadRequestException("Username already exists: " + requestDto.getUsername());
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BadRequestException("Email already exists: " + requestDto.getEmail());
        }

        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + requestDto.getRoleId()));

        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .firstname(requestDto.getFirstname())
                .lastname(requestDto.getLastname())
                .language(requestDto.getLanguage())
                .enabled(requestDto.getEnabled() != null ? requestDto.getEnabled() : true)
                .role(role)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (requestDto.getUsername() != null && !requestDto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(requestDto.getUsername())) {
                throw new BadRequestException("Username already exists: " + requestDto.getUsername());
            }

            user.setUsername(requestDto.getUsername());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new BadRequestException("Email already exists: " + requestDto.getEmail());
            }

            user.setEmail(requestDto.getEmail());
        }

        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + requestDto.getRoleId()));

        user.setFirstname(requestDto.getFirstname());
        user.setLastname(requestDto.getLastname());
        user.setLanguage(requestDto.getLanguage());
        user.setEnabled(requestDto.getEnabled() != null ? requestDto.getEnabled() : user.getEnabled());
        user.setRole(role);

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    private UserResponseDto mapToResponse(User user) {
        Role role = user.getRole();

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .language(user.getLanguage())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .roleId(role != null ? role.getId() : null)
                .roleName(role != null ? role.getRoleName() : null)
                .build();
    }
}