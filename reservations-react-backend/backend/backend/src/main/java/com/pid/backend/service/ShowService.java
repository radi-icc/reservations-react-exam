package com.pid.backend.service;

import com.pid.backend.dto.ShowRequestDto;
import com.pid.backend.dto.ShowResponseDto;
import com.pid.backend.entity.Location;
import com.pid.backend.entity.Show;
import com.pid.backend.entity.User;
import com.pid.backend.repository.LocationRepository;
import com.pid.backend.repository.ShowRepository;
import com.pid.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public List<ShowResponseDto> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShowResponseDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));

        return mapToResponse(show);
    }

    public ShowResponseDto createShow(ShowRequestDto requestDto) {
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + requestDto.getLocationId()));

        User producer = findProducer(requestDto.getProducerId());
        Show show = Show.builder()
                .location(location)
                .producer(producer)
                .slug(generateSlug(requestDto.getTitle()))
                .title(requestDto.getTitle())
                .posterUrl(requestDto.getPosterUrl())
                .bookable(requestDto.getBookable() != null ? requestDto.getBookable() : true)
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(showRepository.save(show));
    }

    public ShowResponseDto updateShow(Long id, ShowRequestDto requestDto) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));

        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + requestDto.getLocationId()));

        show.setLocation(location);
        show.setProducer(findProducer(requestDto.getProducerId()));
        show.setSlug(generateSlug(requestDto.getTitle()));
        show.setTitle(requestDto.getTitle());
        show.setPosterUrl(requestDto.getPosterUrl());
        show.setBookable(requestDto.getBookable() != null ? requestDto.getBookable() : true);
        show.setPrice(requestDto.getPrice());
        show.setDescription(requestDto.getDescription());

        return mapToResponse(showRepository.save(show));
    }

    public void deleteShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));

        showRepository.delete(show);
    }

    private ShowResponseDto mapToResponse(Show show) {
        Location location = show.getLocation();
        User producer = show.getProducer();

        return ShowResponseDto.builder()
                .id(show.getId())
                .locationId(location != null ? location.getId() : null)
                .locationDesignation(location != null ? location.getDesignation() : null)
                .producerId(producer != null ? producer.getId() : null)
                .producerName(producer != null ? producer.getUsername() : null)
                .slug(show.getSlug())
                .title(show.getTitle())
                .posterUrl(show.getPosterUrl())
                .bookable(show.getBookable())
                .price(show.getPrice())
                .description(show.getDescription())
                .createdAt(show.getCreatedAt())
                .build();
    }

    private User findProducer(Long producerId) {
        if (producerId == null) return null;
        User producer = userRepository.findById(producerId).orElseThrow(() -> new RuntimeException("Producer not found with id: " + producerId));
        if (!"PRODUCER".equalsIgnoreCase(producer.getRole() != null ? producer.getRole().getRoleName() : "")) throw new RuntimeException("The selected user is not a producer");
        return producer;
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

    public Page<ShowResponseDto> searchShows(
            String search,
            Long locationId,
            Boolean bookable,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return showRepository.searchShows(search, locationId, bookable, pageable)
                .map(this::mapToResponse);
    }
}
