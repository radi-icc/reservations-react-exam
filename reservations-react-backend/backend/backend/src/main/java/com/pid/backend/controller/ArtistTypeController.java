package com.pid.backend.controller;

import com.pid.backend.dto.ArtistTypeRequestDto;
import com.pid.backend.dto.ArtistTypeResponseDto;
import com.pid.backend.service.ArtistTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artist-types")
@RequiredArgsConstructor
public class ArtistTypeController {

    private final ArtistTypeService artistTypeService;

    @GetMapping
    public List<ArtistTypeResponseDto> getAllArtistTypes() {
        return artistTypeService.getAllArtistTypes();
    }

    @GetMapping("/{id}")
    public ArtistTypeResponseDto getArtistTypeById(@PathVariable Long id) {
        return artistTypeService.getArtistTypeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistTypeResponseDto createArtistType(@Valid @RequestBody ArtistTypeRequestDto requestDto) {
        return artistTypeService.createArtistType(requestDto);
    }

    @PutMapping("/{id}")
    public ArtistTypeResponseDto updateArtistType(
            @PathVariable Long id,
            @Valid @RequestBody ArtistTypeRequestDto requestDto
    ) {
        return artistTypeService.updateArtistType(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtistType(@PathVariable Long id) {
        artistTypeService.deleteArtistType(id);
    }
}