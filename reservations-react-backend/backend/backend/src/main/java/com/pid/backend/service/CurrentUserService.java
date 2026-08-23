package com.pid.backend.service;

import com.pid.backend.entity.User;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    public boolean isAdmin(User user) {
        return user != null
                && user.getRole() != null
                && "ADMIN".equalsIgnoreCase(user.getRole().getRoleName());
    }

    public boolean hasRole(User user, String roleName) {
        return user != null && user.getRole() != null && roleName.equalsIgnoreCase(user.getRole().getRoleName());
    }
}
