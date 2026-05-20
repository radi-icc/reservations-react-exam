package com.pid.backend.service;

import com.pid.backend.dto.ArtistTypeRequestDto;
import com.pid.backend.dto.ArtistTypeResponseDto;
import com.pid.backend.entity.ArtistType;
import com.pid.backend.repository.ArtistTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistTypeService {

    private final ArtistTypeRepository artistTypeRepository;

    public List<ArtistTypeResponseDto> getAllArtistTypes() {
        return artistTypeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ArtistTypeResponseDto getArtistTypeById(Long id) {
        ArtistType artistType = artistTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + id));

        return mapToResponse(artistType);
    }

    public ArtistTypeResponseDto createArtistType(ArtistTypeRequestDto requestDto) {
        ArtistType artistType = ArtistType.builder()
                .typeName(requestDto.getTypeName())
                .build();

        return mapToResponse(artistTypeRepository.save(artistType));
    }

    public ArtistTypeResponseDto updateArtistType(Long id, ArtistTypeRequestDto requestDto) {
        ArtistType artistType = artistTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + id));

        artistType.setTypeName(requestDto.getTypeName());

        return mapToResponse(artistTypeRepository.save(artistType));
    }

    public void deleteArtistType(Long id) {
        ArtistType artistType = artistTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + id));

        artistTypeRepository.delete(artistType);
    }

    private ArtistTypeResponseDto mapToResponse(ArtistType artistType) {
        return ArtistTypeResponseDto.builder()
                .id(artistType.getId())
                .typeName(artistType.getTypeName())
                .build();
    }
}