package com.pid.backend.service;

import com.pid.backend.dto.ArtistRequestDto;
import com.pid.backend.dto.ArtistResponseDto;
import com.pid.backend.entity.Artist;
import com.pid.backend.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public List<ArtistResponseDto> getAllArtists() {
        return artistRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ArtistResponseDto getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));

        return mapToResponseDto(artist);
    }

    public ArtistResponseDto createArtist(ArtistRequestDto requestDto) {
        Artist artist = Artist.builder()
                .firstname(requestDto.getFirstname())
                .lastname(requestDto.getLastname())
                .build();

        Artist savedArtist = artistRepository.save(artist);

        return mapToResponseDto(savedArtist);
    }

    public ArtistResponseDto updateArtist(Long id, ArtistRequestDto requestDto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));

        artist.setFirstname(requestDto.getFirstname());
        artist.setLastname(requestDto.getLastname());

        Artist updatedArtist = artistRepository.save(artist);

        return mapToResponseDto(updatedArtist);
    }

    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));

        artistRepository.delete(artist);
    }

    private ArtistResponseDto mapToResponseDto(Artist artist) {
        return ArtistResponseDto.builder()
                .id(artist.getId())
                .firstname(artist.getFirstname())
                .lastname(artist.getLastname())
                .build();
    }
}