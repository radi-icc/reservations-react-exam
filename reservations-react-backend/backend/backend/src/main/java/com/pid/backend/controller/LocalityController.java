package com.pid.backend.controller;

import com.pid.backend.dto.LocalityRequestDto;
import com.pid.backend.dto.LocalityResponseDto;
import com.pid.backend.service.LocalityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localities")
@RequiredArgsConstructor
public class LocalityController {

    private final LocalityService localityService;

    @GetMapping
    public List<LocalityResponseDto> getAllLocalities() {
        return localityService.getAllLocalities();
    }

    @GetMapping("/{id}")
    public LocalityResponseDto getLocalityById(@PathVariable Long id) {
        return localityService.getLocalityById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocalityResponseDto createLocality(@Valid @RequestBody LocalityRequestDto requestDto) {
        return localityService.createLocality(requestDto);
    }

    @PutMapping("/{id}")
    public LocalityResponseDto updateLocality(
            @PathVariable Long id,
            @Valid @RequestBody LocalityRequestDto requestDto
    ) {
        return localityService.updateLocality(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocality(@PathVariable Long id) {
        localityService.deleteLocality(id);
    }
}