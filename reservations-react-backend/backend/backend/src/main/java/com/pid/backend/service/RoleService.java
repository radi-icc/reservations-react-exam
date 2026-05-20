package com.pid.backend.service;

import com.pid.backend.dto.RoleRequestDto;
import com.pid.backend.dto.RoleResponseDto;
import com.pid.backend.entity.Role;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for role management.
 *
 * Roles are used for access control such as ADMIN, MEMBER, PRODUCER,
 * CRITIC and AFFILIATE.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        return mapToResponse(role);
    }

    public RoleResponseDto createRole(RoleRequestDto requestDto) {
        String roleName = normalizeRoleName(requestDto.getRoleName());

        if (roleRepository.existsByRoleName(roleName)) {
            throw new BadRequestException("Role already exists: " + roleName);
        }

        Role role = Role.builder()
                .roleName(roleName)
                .build();

        return mapToResponse(roleRepository.save(role));
    }

    public RoleResponseDto updateRole(Long id, RoleRequestDto requestDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        String roleName = normalizeRoleName(requestDto.getRoleName());

        roleRepository.findByRoleName(roleName)
                .filter(existingRole -> !existingRole.getId().equals(id))
                .ifPresent(existingRole -> {
                    throw new BadRequestException("Role already exists: " + roleName);
                });

        role.setRoleName(roleName);

        return mapToResponse(roleRepository.save(role));
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        roleRepository.delete(role);
    }

    private String normalizeRoleName(String roleName) {
        return roleName.trim().toUpperCase();
    }

    private RoleResponseDto mapToResponse(Role role) {
        return RoleResponseDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }
}