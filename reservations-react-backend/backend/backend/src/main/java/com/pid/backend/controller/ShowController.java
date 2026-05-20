package com.pid.backend.controller;

import com.pid.backend.dto.ShowRequestDto;
import com.pid.backend.dto.ShowResponseDto;
import com.pid.backend.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @GetMapping
    public Page<ShowResponseDto> getAllShows(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Boolean bookable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return showService.searchShows(search, locationId, bookable, page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public ShowResponseDto getShowById(@PathVariable Long id) {
        return showService.getShowById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowResponseDto createShow(@Valid @RequestBody ShowRequestDto requestDto) {
        return showService.createShow(requestDto);
    }

    @PutMapping("/{id}")
    public ShowResponseDto updateShow(
            @PathVariable Long id,
            @Valid @RequestBody ShowRequestDto requestDto
    ) {
        return showService.updateShow(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
    }


}