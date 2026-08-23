package com.pid.backend.controller;

import com.pid.backend.dto.ApiKeyResponseDto;
import com.pid.backend.dto.AffiliatePlanResponseDto;
import com.pid.backend.service.AffiliatePlanService;
import com.pid.backend.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/affiliate")
@RequiredArgsConstructor
public class AffiliatePortalController {
    private final ApiKeyService apiKeyService;
    private final AffiliatePlanService affiliatePlanService;
    @GetMapping("/plans") public List<AffiliatePlanResponseDto> plans() { return affiliatePlanService.getAllPlans(); }
    @GetMapping("/me/keys") public List<ApiKeyResponseDto> keys() { return apiKeyService.getMyApiKeys(); }
    @PostMapping("/me/keys") @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyResponseDto createKey(@RequestParam Long affiliatePlanId) { return apiKeyService.createMyApiKey(affiliatePlanId); }
}
