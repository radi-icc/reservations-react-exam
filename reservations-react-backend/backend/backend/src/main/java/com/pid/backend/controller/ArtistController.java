package com.pid.backend.controller;

import com.pid.backend.dto.ArtistRequestDto;
import com.pid.backend.dto.ArtistResponseDto;
import com.pid.backend.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public List<ArtistResponseDto> getAllArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{id}")
    public ArtistResponseDto getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistResponseDto createArtist(@Valid @RequestBody ArtistRequestDto requestDto) {
        return artistService.createArtist(requestDto);
    }

    @PutMapping("/{id}")
    public ArtistResponseDto updateArtist(
            @PathVariable Long id,
            @Valid @RequestBody ArtistRequestDto requestDto
    ) {
        return artistService.updateArtist(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
    }
}