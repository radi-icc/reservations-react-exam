package com.pid.backend.service;

import com.pid.backend.dto.AffiliatePlanRequestDto;
import com.pid.backend.dto.AffiliatePlanResponseDto;
import com.pid.backend.entity.AffiliatePlan;
import com.pid.backend.repository.AffiliatePlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AffiliatePlanService {

    private final AffiliatePlanRepository affiliatePlanRepository;

    public List<AffiliatePlanResponseDto> getAllPlans() {
        return affiliatePlanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AffiliatePlanResponseDto getPlanById(Long id) {
        AffiliatePlan plan = affiliatePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affiliate plan not found with id: " + id));

        return mapToResponse(plan);
    }

    public AffiliatePlanResponseDto createPlan(AffiliatePlanRequestDto requestDto) {
        AffiliatePlan plan = AffiliatePlan.builder()
                .planName(requestDto.getPlanName())
                .apiLimit(requestDto.getApiLimit())
                .monthlyPrice(requestDto.getMonthlyPrice())
                .build();

        return mapToResponse(affiliatePlanRepository.save(plan));
    }

    public AffiliatePlanResponseDto updatePlan(Long id, AffiliatePlanRequestDto requestDto) {
        AffiliatePlan plan = affiliatePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affiliate plan not found with id: " + id));

        plan.setPlanName(requestDto.getPlanName());
        plan.setApiLimit(requestDto.getApiLimit());
        plan.setMonthlyPrice(requestDto.getMonthlyPrice());

        return mapToResponse(affiliatePlanRepository.save(plan));
    }

    public void deletePlan(Long id) {
        AffiliatePlan plan = affiliatePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affiliate plan not found with id: " + id));

        affiliatePlanRepository.delete(plan);
    }

    private AffiliatePlanResponseDto mapToResponse(AffiliatePlan plan) {
        return AffiliatePlanResponseDto.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .apiLimit(plan.getApiLimit())
                .monthlyPrice(plan.getMonthlyPrice())
                .build();
    }
}