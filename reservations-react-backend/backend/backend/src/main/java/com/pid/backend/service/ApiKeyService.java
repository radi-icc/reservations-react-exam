package com.pid.backend.service;

import com.pid.backend.dto.ApiKeyRequestDto;
import com.pid.backend.dto.ApiKeyResponseDto;
import com.pid.backend.entity.AffiliatePlan;
import com.pid.backend.entity.ApiKey;
import com.pid.backend.entity.User;
import com.pid.backend.repository.AffiliatePlanRepository;
import com.pid.backend.repository.ApiKeyRepository;
import com.pid.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final AffiliatePlanRepository affiliatePlanRepository;

    public List<ApiKeyResponseDto> getAllApiKeys() {
        return apiKeyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ApiKeyResponseDto getApiKeyById(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API key not found with id: " + id));

        return mapToResponse(apiKey);
    }

    public ApiKeyResponseDto createApiKey(ApiKeyRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDto.getUserId()));

        AffiliatePlan affiliatePlan = affiliatePlanRepository.findById(requestDto.getAffiliatePlanId())
                .orElseThrow(() -> new RuntimeException("Affiliate plan not found with id: " + requestDto.getAffiliatePlanId()));

        ApiKey apiKey = ApiKey.builder()
                .user(user)
                .affiliatePlan(affiliatePlan)
                .apiKey(generateSecureApiKey())
                .enabled(true)
                .build();

        return mapToResponse(apiKeyRepository.save(apiKey));
    }

    public ApiKeyResponseDto disableApiKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API key not found with id: " + id));

        apiKey.setEnabled(false);

        return mapToResponse(apiKeyRepository.save(apiKey));
    }

    public ApiKeyResponseDto enableApiKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API key not found with id: " + id));

        apiKey.setEnabled(true);

        return mapToResponse(apiKeyRepository.save(apiKey));
    }

    public void deleteApiKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API key not found with id: " + id));

        apiKeyRepository.delete(apiKey);
    }

    private String generateSecureApiKey() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private ApiKeyResponseDto mapToResponse(ApiKey apiKey) {
        User user = apiKey.getUser();
        AffiliatePlan affiliatePlan = apiKey.getAffiliatePlan();

        return ApiKeyResponseDto.builder()
                .id(apiKey.getId())
                .userId(user != null ? user.getId() : null)
                .username(user != null ? user.getUsername() : null)
                .email(user != null ? user.getEmail() : null)
                .affiliatePlanId(affiliatePlan != null ? affiliatePlan.getId() : null)
                .affiliatePlanName(affiliatePlan != null ? affiliatePlan.getPlanName() : null)
                .apiKey(apiKey.getApiKey())
                .enabled(apiKey.getEnabled())
                .createdAt(apiKey.getCreatedAt())
                .build();
    }
}