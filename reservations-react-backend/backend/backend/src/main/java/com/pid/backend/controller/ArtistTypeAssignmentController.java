package com.pid.backend.controller;

import com.pid.backend.dto.ArtistTypeAssignmentRequestDto;
import com.pid.backend.dto.ArtistTypeAssignmentResponseDto;
import com.pid.backend.service.ArtistTypeAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artist-type-assignments")
@RequiredArgsConstructor
public class ArtistTypeAssignmentController {

    private final ArtistTypeAssignmentService artistTypeAssignmentService;

    @GetMapping
    public List<ArtistTypeAssignmentResponseDto> getAllAssignments() {
        return artistTypeAssignmentService.getAllAssignments();
    }

    @GetMapping("/{id}")
    public ArtistTypeAssignmentResponseDto getAssignmentById(@PathVariable Long id) {
        return artistTypeAssignmentService.getAssignmentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistTypeAssignmentResponseDto createAssignment(
            @Valid @RequestBody ArtistTypeAssignmentRequestDto requestDto
    ) {
        return artistTypeAssignmentService.createAssignment(requestDto);
    }

    @PutMapping("/{id}")
    public ArtistTypeAssignmentResponseDto updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody ArtistTypeAssignmentRequestDto requestDto
    ) {
        return artistTypeAssignmentService.updateAssignment(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(@PathVariable Long id) {
        artistTypeAssignmentService.deleteAssignment(id);
    }
}