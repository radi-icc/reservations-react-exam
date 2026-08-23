package com.pid.backend.controller;

import com.pid.backend.dto.ShowResponseDto;
import com.pid.backend.service.AffiliateCatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affiliate")
@RequiredArgsConstructor
public class AffiliateCatalogueController {

    private final AffiliateCatalogueService affiliateCatalogueService;

    @GetMapping("/shows")
    public List<ShowResponseDto> getAffiliateShows(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey
    ) {
        return affiliateCatalogueService.getShowsForAffiliate(apiKey);
    }
}
