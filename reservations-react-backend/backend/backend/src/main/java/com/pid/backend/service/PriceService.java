package com.pid.backend.service;

import com.pid.backend.dto.PriceRequestDto;
import com.pid.backend.dto.PriceResponseDto;
import com.pid.backend.entity.Price;
import com.pid.backend.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    public List<PriceResponseDto> getAllPrices() {
        return priceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PriceResponseDto getPriceById(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        return mapToResponse(price);
    }

    public PriceResponseDto createPrice(PriceRequestDto requestDto) {
        Price price = Price.builder()
                .label(requestDto.getLabel())
                .amount(requestDto.getAmount())
                .build();

        return mapToResponse(priceRepository.save(price));
    }

    public PriceResponseDto updatePrice(Long id, PriceRequestDto requestDto) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        price.setLabel(requestDto.getLabel());
        price.setAmount(requestDto.getAmount());

        return mapToResponse(priceRepository.save(price));
    }

    public void deletePrice(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        priceRepository.delete(price);
    }

    private PriceResponseDto mapToResponse(Price price) {
        return PriceResponseDto.builder()
                .id(price.getId())
                .label(price.getLabel())
                .amount(price.getAmount())
                .build();
    }
}