package com.pid.backend.controller;

import com.pid.backend.dto.ReviewRequestDto;
import com.pid.backend.dto.ReviewResponseDto;
import com.pid.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/critic")
@RequiredArgsConstructor
public class CriticController {
    private final ReviewService reviewService;
    @PostMapping("/reviews") @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto submit(@Valid @RequestBody ReviewRequestDto request, @RequestParam(required = false) String sourceUrl) { return reviewService.createCritique(request, sourceUrl); }
}
