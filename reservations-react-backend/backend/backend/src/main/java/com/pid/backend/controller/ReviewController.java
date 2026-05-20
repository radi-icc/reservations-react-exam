package com.pid.backend.controller;

import com.pid.backend.dto.ReviewRequestDto;
import com.pid.backend.dto.ReviewResponseDto;
import com.pid.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewResponseDto> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ReviewResponseDto getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto createReview(@Valid @RequestBody ReviewRequestDto requestDto) {
        return reviewService.createReview(requestDto);
    }

    @PatchMapping("/{id}/publish")
    public ReviewResponseDto publishReview(@PathVariable Long id) {
        return reviewService.publishReview(id);
    }

    @PatchMapping("/{id}/unpublish")
    public ReviewResponseDto unpublishReview(@PathVariable Long id) {
        return reviewService.unpublishReview(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}