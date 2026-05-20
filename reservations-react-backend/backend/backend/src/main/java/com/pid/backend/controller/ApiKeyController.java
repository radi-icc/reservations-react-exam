package com.pid.backend.controller;

import com.pid.backend.dto.ApiKeyRequestDto;
import com.pid.backend.dto.ApiKeyResponseDto;
import com.pid.backend.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @GetMapping
    public List<ApiKeyResponseDto> getAllApiKeys() {
        return apiKeyService.getAllApiKeys();
    }

    @GetMapping("/{id}")
    public ApiKeyResponseDto getApiKeyById(@PathVariable Long id) {
        return apiKeyService.getApiKeyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyResponseDto createApiKey(@Valid @RequestBody ApiKeyRequestDto requestDto) {
        return apiKeyService.createApiKey(requestDto);
    }

    @PatchMapping("/{id}/disable")
    public ApiKeyResponseDto disableApiKey(@PathVariable Long id) {
        return apiKeyService.disableApiKey(id);
    }

    @PatchMapping("/{id}/enable")
    public ApiKeyResponseDto enableApiKey(@PathVariable Long id) {
        return apiKeyService.enableApiKey(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApiKey(@PathVariable Long id) {
        apiKeyService.deleteApiKey(id);
    }
}