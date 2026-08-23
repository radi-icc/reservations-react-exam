package com.pid.backend.service;

import com.pid.backend.dto.ShowResponseDto;
import com.pid.backend.entity.ApiKey;
import com.pid.backend.entity.Location;
import com.pid.backend.entity.Show;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.repository.ApiKeyRepository;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AffiliateCatalogueService {

    private final ApiKeyRepository apiKeyRepository;
    private final ShowRepository showRepository;

    @Transactional
    public List<ShowResponseDto> getShowsForAffiliate(String apiKeyValue) {
        ApiKey apiKey = validateApiKey(apiKeyValue);

        String planName = apiKey.getAffiliatePlan() != null
                ? apiKey.getAffiliatePlan().getPlanName()
                : "FREE";

        List<Show> shows = showRepository.findAll();

        if ("FREE".equalsIgnoreCase(planName)) {
            return shows.stream()
                    .limit(5)
                    .map(this::mapToLimitedResponse)
                    .toList();
        }

        return shows.stream()
                .map(this::mapToFullResponse)
                .toList();
    }

    private ApiKey validateApiKey(String apiKeyValue) {
        if (apiKeyValue == null || apiKeyValue.isBlank()) {
            throw new BadRequestException("X-API-KEY header is required");
        }

        ApiKey apiKey = apiKeyRepository.findByApiKey(apiKeyValue)
                .orElseThrow(() -> new BadRequestException("Invalid API key"));

        if (!Boolean.TRUE.equals(apiKey.getEnabled())) {
            throw new BadRequestException("API key is disabled");
        }

        int limit = apiKey.getAffiliatePlan() != null && apiKey.getAffiliatePlan().getApiLimit() != null
                ? apiKey.getAffiliatePlan().getApiLimit() : 0;
        LocalDateTime now = LocalDateTime.now();
        if (apiKey.getApiUsagePeriodStart() == null || !apiKey.getApiUsagePeriodStart().toLocalDate().withDayOfMonth(1).equals(now.toLocalDate().withDayOfMonth(1))) {
            apiKey.setApiUsageCount(0);
            apiKey.setApiUsagePeriodStart(now);
        }
        int usage = apiKey.getApiUsageCount() != null ? apiKey.getApiUsageCount() : 0;
        if (limit > 0 && usage >= limit) {
            throw new BadRequestException("Monthly API quota reached for this affiliate plan");
        }
        apiKey.setApiUsageCount(usage + 1);
        apiKeyRepository.save(apiKey);

        return apiKey;
    }

    private ShowResponseDto mapToLimitedResponse(Show show) {
        Location location = show.getLocation();

        return ShowResponseDto.builder()
                .id(show.getId())
                .title(show.getTitle())
                .slug(show.getSlug())
                .bookable(show.getBookable())
                .locationId(location != null ? location.getId() : null)
                .locationDesignation(location != null ? location.getDesignation() : null)
                .build();
    }

    private ShowResponseDto mapToFullResponse(Show show) {
        Location location = show.getLocation();

        return ShowResponseDto.builder()
                .id(show.getId())
                .locationId(location != null ? location.getId() : null)
                .locationDesignation(location != null ? location.getDesignation() : null)
                .slug(show.getSlug())
                .title(show.getTitle())
                .posterUrl(show.getPosterUrl())
                .bookable(show.getBookable())
                .price(show.getPrice())
                .description(show.getDescription())
                .createdAt(show.getCreatedAt())
                .build();
    }
}
