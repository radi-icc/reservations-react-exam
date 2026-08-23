package com.pid.backend.service;

import com.pid.backend.dto.PriceRequestDto;
import com.pid.backend.dto.PriceResponseDto;
import com.pid.backend.entity.Price;
import com.pid.backend.entity.Representation;
import com.pid.backend.repository.PriceRepository;
import com.pid.backend.repository.RepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final RepresentationRepository representationRepository;

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

    public List<PriceResponseDto> getPricesForRepresentation(Long representationId) {
        return priceRepository.findByRepresentationId(representationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PriceResponseDto createPrice(PriceRequestDto requestDto) {
        Representation representation = getRepresentation(requestDto.getRepresentationId());

        Price price = Price.builder()
                .label(requestDto.getLabel())
                .amount(requestDto.getAmount())
                .representation(representation)
                .build();

        return mapToResponse(priceRepository.save(price));
    }

    public PriceResponseDto updatePrice(Long id, PriceRequestDto requestDto) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        price.setRepresentation(getRepresentation(requestDto.getRepresentationId()));
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
        Representation representation = price.getRepresentation();

        return PriceResponseDto.builder()
                .id(price.getId())
                .label(price.getLabel())
                .amount(price.getAmount())
                .representationId(representation != null ? representation.getId() : null)
                .showTitle(representation != null && representation.getShow() != null ? representation.getShow().getTitle() : null)
                .performanceDate(representation != null && representation.getPerformanceDate() != null ? representation.getPerformanceDate().toString() : null)
                .performanceTime(representation != null && representation.getPerformanceTime() != null ? representation.getPerformanceTime().toString() : null)
                .build();
    }

    private Representation getRepresentation(Long representationId) {
        return representationRepository.findById(representationId)
                .orElseThrow(() -> new RuntimeException("Representation not found with id: " + representationId));
    }
}
