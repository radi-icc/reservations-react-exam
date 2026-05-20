package com.pid.backend.service;

import com.pid.backend.dto.*;
import com.pid.backend.entity.Location;
import com.pid.backend.entity.Representation;
import com.pid.backend.entity.Show;
import com.pid.backend.repository.LocationRepository;
import com.pid.backend.repository.RepresentationRepository;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepresentationService {

    private final RepresentationRepository representationRepository;
    private final ShowRepository showRepository;
    private final LocationRepository locationRepository;

    public List<RepresentationResponseDto> getAllRepresentations() {
        return representationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RepresentationResponseDto getRepresentationById(Long id) {
        Representation representation = representationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Representation not found with id: " + id));

        return mapToResponse(representation);
    }

    public RepresentationResponseDto createRepresentation(RepresentationRequestDto requestDto) {
        Show show = showRepository.findById(requestDto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + requestDto.getShowId()));

        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + requestDto.getLocationId()));

        Integer capacity = requestDto.getCapacity() != null ? requestDto.getCapacity() : 0;
        Integer bookedSeats = requestDto.getBookedSeats() != null ? requestDto.getBookedSeats() : 0;

        Representation representation = Representation.builder()
                .show(show)
                .location(location)
                .performanceDate(requestDto.getPerformanceDate())
                .performanceTime(requestDto.getPerformanceTime())
                .capacity(capacity)
                .bookedSeats(bookedSeats)
                .full(bookedSeats >= capacity && capacity > 0)
                .build();

        return mapToResponse(representationRepository.save(representation));
    }

    public RepresentationResponseDto updateRepresentation(Long id, RepresentationRequestDto requestDto) {
        Representation representation = representationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Representation not found with id: " + id));

        Show show = showRepository.findById(requestDto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + requestDto.getShowId()));

        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + requestDto.getLocationId()));

        Integer capacity = requestDto.getCapacity() != null ? requestDto.getCapacity() : 0;
        Integer bookedSeats = requestDto.getBookedSeats() != null ? requestDto.getBookedSeats() : 0;

        representation.setShow(show);
        representation.setLocation(location);
        representation.setPerformanceDate(requestDto.getPerformanceDate());
        representation.setPerformanceTime(requestDto.getPerformanceTime());
        representation.setCapacity(capacity);
        representation.setBookedSeats(bookedSeats);
        representation.setFull(bookedSeats >= capacity && capacity > 0);

        return mapToResponse(representationRepository.save(representation));
    }

    public void deleteRepresentation(Long id) {
        Representation representation = representationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Representation not found with id: " + id));

        representationRepository.delete(representation);
    }

    private RepresentationResponseDto mapToResponse(Representation representation) {
        Show show = representation.getShow();
        Location location = representation.getLocation();

        return RepresentationResponseDto.builder()
                .id(representation.getId())
                .showId(show != null ? show.getId() : null)
                .showTitle(show != null ? show.getTitle() : null)
                .locationId(location != null ? location.getId() : null)
                .locationDesignation(location != null ? location.getDesignation() : null)
                .performanceDate(representation.getPerformanceDate())
                .performanceTime(representation.getPerformanceTime())
                .capacity(representation.getCapacity())
                .bookedSeats(representation.getBookedSeats())
                .full(representation.getFull())
                .build();
    }
    public AvailabilityResponseDto getAvailability(Long id) {
        Representation representation = representationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Representation not found with id: " + id));

        int capacity = representation.getCapacity() != null ? representation.getCapacity() : 0;
        int bookedSeats = representation.getBookedSeats() != null ? representation.getBookedSeats() : 0;

        return AvailabilityResponseDto.builder()
                .representationId(representation.getId())
                .capacity(capacity)
                .bookedSeats(bookedSeats)
                .availableSeats(Math.max(0, capacity - bookedSeats))
                .full(representation.getFull())
                .build();
    }
}