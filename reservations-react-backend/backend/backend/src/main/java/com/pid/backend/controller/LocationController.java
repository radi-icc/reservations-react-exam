package com.pid.backend.controller;

import com.pid.backend.dto.LocationRequestDto;
import com.pid.backend.dto.LocationResponseDto;
import com.pid.backend.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationResponseDto> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public LocationResponseDto getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDto createLocation(@Valid @RequestBody LocationRequestDto requestDto) {
        return locationService.createLocation(requestDto);
    }

    @PutMapping("/{id}")
    public LocationResponseDto updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequestDto requestDto
    ) {
        return locationService.updateLocation(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}