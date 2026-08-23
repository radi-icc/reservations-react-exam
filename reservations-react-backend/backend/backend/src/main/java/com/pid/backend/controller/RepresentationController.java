package com.pid.backend.controller;

import com.pid.backend.dto.*;
import com.pid.backend.service.RepresentationService;
import com.pid.backend.service.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/representations")
@RequiredArgsConstructor
public class RepresentationController {

    private final RepresentationService representationService;
    private final PriceService priceService;

    @GetMapping
    public List<RepresentationResponseDto> getAllRepresentations(
            @RequestParam(required = false) Long showId
    ) {
        return representationService.getAllRepresentations(showId);
    }

    @GetMapping("/{id}")
    public RepresentationResponseDto getRepresentationById(@PathVariable Long id) {
        return representationService.getRepresentationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RepresentationResponseDto createRepresentation(
            @Valid @RequestBody RepresentationRequestDto requestDto
    ) {
        return representationService.createRepresentation(requestDto);
    }

    @PutMapping("/{id}")
    public RepresentationResponseDto updateRepresentation(
            @PathVariable Long id,
            @Valid @RequestBody RepresentationRequestDto requestDto
    ) {
        return representationService.updateRepresentation(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRepresentation(@PathVariable Long id) {
        representationService.deleteRepresentation(id);
    }

    @GetMapping("/{id}/availability")
    public AvailabilityResponseDto getAvailability(@PathVariable Long id) {
        return representationService.getAvailability(id);
    }

    @GetMapping("/{id}/prices")
    public List<PriceResponseDto> getPrices(@PathVariable Long id) {
        return priceService.getPricesForRepresentation(id);
    }

}
