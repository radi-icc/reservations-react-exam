package com.pid.backend.controller;

import com.pid.backend.dto.PriceRequestDto;
import com.pid.backend.dto.PriceResponseDto;
import com.pid.backend.service.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public List<PriceResponseDto> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/{id}")
    public PriceResponseDto getPriceById(@PathVariable Long id) {
        return priceService.getPriceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PriceResponseDto createPrice(@Valid @RequestBody PriceRequestDto requestDto) {
        return priceService.createPrice(requestDto);
    }

    @PutMapping("/{id}")
    public PriceResponseDto updatePrice(
            @PathVariable Long id,
            @Valid @RequestBody PriceRequestDto requestDto
    ) {
        return priceService.updatePrice(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
    }
}