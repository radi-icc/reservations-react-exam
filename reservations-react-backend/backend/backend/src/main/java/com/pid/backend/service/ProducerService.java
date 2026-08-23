package com.pid.backend.service;

import com.pid.backend.dto.ReviewResponseDto;
import com.pid.backend.dto.ShowResponseDto;
import com.pid.backend.dto.ShowSalesStatsResponseDto;
import com.pid.backend.entity.User;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final CurrentUserService currentUserService;
    private final ShowRepository showRepository;
    private final ShowService showService;
    private final StatisticsService statisticsService;
    private final ReviewService reviewService;
    public List<ShowResponseDto> getMyShows() { return showRepository.findByProducerId(producer().getId()).stream().map(show -> showService.getShowById(show.getId())).toList(); }
    public ShowSalesStatsResponseDto getSales(Long showId) {
        if (!showRepository.findByProducerId(producer().getId()).stream().anyMatch(show -> show.getId().equals(showId))) throw new BadRequestException("You do not produce this show");
        return statisticsService.getShowSalesStats(showId);
    }
    public List<ReviewResponseDto> getReviews() { producer(); return reviewService.getReviewsForProducer(); }
    public ReviewResponseDto publish(Long id) { producer(); return reviewService.publishReview(id); }
    public ReviewResponseDto unpublish(Long id) { producer(); return reviewService.unpublishReview(id); }
    private User producer() { User user=currentUserService.getCurrentUser(); if (!currentUserService.hasRole(user,"PRODUCER")) throw new BadRequestException("Producer role is required"); return user; }
}
