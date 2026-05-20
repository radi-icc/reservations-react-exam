package com.pid.backend.service;

import com.pid.backend.dto.LocationRequestDto;
import com.pid.backend.dto.LocationResponseDto;
import com.pid.backend.entity.Locality;
import com.pid.backend.entity.Location;
import com.pid.backend.repository.LocalityRepository;
import com.pid.backend.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocalityRepository localityRepository;

    public List<LocationResponseDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LocationResponseDto getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        return mapToResponse(location);
    }

    public LocationResponseDto createLocation(LocationRequestDto requestDto) {
        Locality locality = localityRepository.findById(requestDto.getLocalityId())
                .orElseThrow(() -> new RuntimeException("Locality not found with id: " + requestDto.getLocalityId()));

        Location location = Location.builder()
                .locality(locality)
                .slug(generateSlug(requestDto.getDesignation()))
                .designation(requestDto.getDesignation())
                .address(requestDto.getAddress())
                .website(requestDto.getWebsite())
                .phone(requestDto.getPhone())
                .build();

        return mapToResponse(locationRepository.save(location));
    }

    public LocationResponseDto updateLocation(Long id, LocationRequestDto requestDto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        Locality locality = localityRepository.findById(requestDto.getLocalityId())
                .orElseThrow(() -> new RuntimeException("Locality not found with id: " + requestDto.getLocalityId()));

        location.setLocality(locality);
        location.setSlug(generateSlug(requestDto.getDesignation()));
        location.setDesignation(requestDto.getDesignation());
        location.setAddress(requestDto.getAddress());
        location.setWebsite(requestDto.getWebsite());
        location.setPhone(requestDto.getPhone());

        return mapToResponse(locationRepository.save(location));
    }

    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        locationRepository.delete(location);
    }

    private LocationResponseDto mapToResponse(Location location) {
        Locality locality = location.getLocality();

        return LocationResponseDto.builder()
                .id(location.getId())
                .localityId(locality != null ? locality.getId() : null)
                .localityName(locality != null ? locality.getLocality() : null)
                .postalCode(locality != null ? locality.getPostalCode() : null)
                .slug(location.getSlug())
                .designation(location.getDesignation())
                .address(location.getAddress())
                .website(location.getWebsite())
                .phone(location.getPhone())
                .build();
    }

    private String generateSlug(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}