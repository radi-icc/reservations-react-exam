package com.pid.backend.controller;

import com.pid.backend.dto.ReviewResponseDto;
import com.pid.backend.dto.ShowResponseDto;
import com.pid.backend.dto.ShowSalesStatsResponseDto;
import com.pid.backend.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/producer")
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;
    @GetMapping("/shows") public List<ShowResponseDto> shows() { return producerService.getMyShows(); }
    @GetMapping("/shows/{showId}/sales") public ShowSalesStatsResponseDto sales(@PathVariable Long showId) { return producerService.getSales(showId); }
    @GetMapping("/reviews") public List<ReviewResponseDto> reviews() { return producerService.getReviews(); }
    @PatchMapping("/reviews/{id}/publish") public ReviewResponseDto publish(@PathVariable Long id) { return producerService.publish(id); }
    @PatchMapping("/reviews/{id}/unpublish") public ReviewResponseDto unpublish(@PathVariable Long id) { return producerService.unpublish(id); }
}
