package com.pid.backend.controller;

import com.pid.backend.dto.GeneralStatisticsResponseDto;
import com.pid.backend.dto.ShowSalesStatsResponseDto;
import com.pid.backend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public GeneralStatisticsResponseDto getGeneralStatistics() {
        return statisticsService.getGeneralStatistics();
    }

    @GetMapping("/shows/{showId}/sales")
    public ShowSalesStatsResponseDto getShowSalesStats(@PathVariable Long showId) {
        return statisticsService.getShowSalesStats(showId);
    }
}