package com.pid.backend.controller;

import com.pid.backend.dto.CollaborationRequestDto;
import com.pid.backend.dto.CollaborationResponseDto;
import com.pid.backend.service.CollaborationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborations")
@RequiredArgsConstructor
public class CollaborationController {

    private final CollaborationService collaborationService;

    @GetMapping
    public List<CollaborationResponseDto> getAllCollaborations() {
        return collaborationService.getAllCollaborations();
    }

    @GetMapping("/{id}")
    public CollaborationResponseDto getCollaborationById(@PathVariable Long id) {
        return collaborationService.getCollaborationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollaborationResponseDto createCollaboration(
            @Valid @RequestBody CollaborationRequestDto requestDto
    ) {
        return collaborationService.createCollaboration(requestDto);
    }

    @PutMapping("/{id}")
    public CollaborationResponseDto updateCollaboration(
            @PathVariable Long id,
            @Valid @RequestBody CollaborationRequestDto requestDto
    ) {
        return collaborationService.updateCollaboration(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollaboration(@PathVariable Long id) {
        collaborationService.deleteCollaboration(id);
    }
}