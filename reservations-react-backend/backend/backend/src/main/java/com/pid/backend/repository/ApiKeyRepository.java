package com.pid.backend.repository;

import com.pid.backend.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findByApiKey(String apiKey);

    List<ApiKey> findByUserId(Long userId);
}
