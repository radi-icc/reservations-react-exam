package com.pid.backend.config;

import com.pid.backend.entity.Role;
import com.pid.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Inserts default roles when the application starts.
 *
 * This prevents manually inserting roles every time the database is empty.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> roles = List.of(
                "ADMIN",
                "MEMBER",
                "PRODUCER",
                "CRITIC",
                "AFFILIATE"
        );

        for (String roleName : roles) {
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = Role.builder()
                        .roleName(roleName)
                        .build();

                roleRepository.save(role);
            }
        }
    }
}