package com.pid.backend.controller;

import com.pid.backend.dto.AffiliatePlanRequestDto;
import com.pid.backend.dto.AffiliatePlanResponseDto;
import com.pid.backend.service.AffiliatePlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affiliate-plans")
@RequiredArgsConstructor
public class AffiliatePlanController {

    private final AffiliatePlanService affiliatePlanService;

    @GetMapping
    public List<AffiliatePlanResponseDto> getAllPlans() {
        return affiliatePlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    public AffiliatePlanResponseDto getPlanById(@PathVariable Long id) {
        return affiliatePlanService.getPlanById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AffiliatePlanResponseDto createPlan(@Valid @RequestBody AffiliatePlanRequestDto requestDto) {
        return affiliatePlanService.createPlan(requestDto);
    }

    @PutMapping("/{id}")
    public AffiliatePlanResponseDto updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody AffiliatePlanRequestDto requestDto
    ) {
        return affiliatePlanService.updatePlan(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable Long id) {
        affiliatePlanService.deletePlan(id);
    }
}