package com.pid.backend.controller;

import com.pid.backend.dto.RoleRequestDto;
import com.pid.backend.dto.RoleResponseDto;
import com.pid.backend.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user roles.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleResponseDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public RoleResponseDto getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto createRole(@Valid @RequestBody RoleRequestDto requestDto) {
        return roleService.createRole(requestDto);
    }

    @PutMapping("/{id}")
    public RoleResponseDto updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDto requestDto
    ) {
        return roleService.updateRole(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}