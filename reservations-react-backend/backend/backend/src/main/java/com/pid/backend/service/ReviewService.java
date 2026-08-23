package com.pid.backend.service;

import com.pid.backend.dto.ReviewRequestDto;
import com.pid.backend.dto.ReviewResponseDto;
import com.pid.backend.entity.Review;
import com.pid.backend.entity.Show;
import com.pid.backend.entity.User;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.ReviewRepository;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import com.pid.backend.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ShowRepository showRepository;
    private final CurrentUserService currentUserService;

    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReviewResponseDto> getPublishedReviewsByShow(Long showId) {
        return reviewRepository.findByShowIdAndPublishedTrue(showId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReviewResponseDto> getMyReviews() {
        User user = currentUserService.getCurrentUser();

        return reviewRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ReviewResponseDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        User currentUser = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(currentUser)
                && !review.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot view another user's review");
        }

        return mapToResponse(review);
    }

    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
        User user = currentUserService.getCurrentUser();

        Show show = showRepository.findById(requestDto.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + requestDto.getShowId()));

        Review review = Review.builder()
                .user(user)
                .show(show)
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .published(false)
                .build();

        return mapToResponse(reviewRepository.save(review));
    }

    public ReviewResponseDto publishReview(Long id) {
        requireAdmin();
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setPublished(true);

        return mapToResponse(reviewRepository.save(review));
    }

    public ReviewResponseDto unpublishReview(Long id) {
        requireAdmin();
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setPublished(false);

        return mapToResponse(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        User currentUser = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(currentUser)
                && !review.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot delete another user's review");
        }

        reviewRepository.delete(review);
    }

    private void requireAdmin() {
        if (!currentUserService.isAdmin(currentUserService.getCurrentUser())) {
            throw new BadRequestException("Administrator role is required");
        }
    }

    private ReviewResponseDto mapToResponse(Review review) {
        User user = review.getUser();
        Show show = review.getShow();

        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(user != null ? user.getId() : null)
                .username(user != null ? user.getUsername() : null)
                .showId(show != null ? show.getId() : null)
                .showTitle(show != null ? show.getTitle() : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .published(review.getPublished())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
