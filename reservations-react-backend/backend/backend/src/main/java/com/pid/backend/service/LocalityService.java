package com.pid.backend.service;

import com.pid.backend.dto.LocalityRequestDto;
import com.pid.backend.dto.LocalityResponseDto;
import com.pid.backend.entity.Locality;
import com.pid.backend.repository.LocalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalityService {

    private final LocalityRepository localityRepository;

    public List<LocalityResponseDto> getAllLocalities() {
        return localityRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LocalityResponseDto getLocalityById(Long id) {
        Locality locality = localityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locality not found with id: " + id));

        return mapToResponse(locality);
    }

    public LocalityResponseDto createLocality(LocalityRequestDto requestDto) {
        Locality locality = Locality.builder()
                .postalCode(requestDto.getPostalCode())
                .locality(requestDto.getLocality())
                .build();

        return mapToResponse(localityRepository.save(locality));
    }

    public LocalityResponseDto updateLocality(Long id, LocalityRequestDto requestDto) {
        Locality locality = localityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locality not found with id: " + id));

        locality.setPostalCode(requestDto.getPostalCode());
        locality.setLocality(requestDto.getLocality());

        return mapToResponse(localityRepository.save(locality));
    }

    public void deleteLocality(Long id) {
        Locality locality = localityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locality not found with id: " + id));

        localityRepository.delete(locality);
    }

    private LocalityResponseDto mapToResponse(Locality locality) {
        return LocalityResponseDto.builder()
                .id(locality.getId())
                .postalCode(locality.getPostalCode())
                .locality(locality.getLocality())
                .build();
    }
}