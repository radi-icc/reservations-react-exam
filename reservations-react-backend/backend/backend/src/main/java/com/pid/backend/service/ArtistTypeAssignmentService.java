package com.pid.backend.service;

import com.pid.backend.dto.ArtistTypeAssignmentRequestDto;
import com.pid.backend.dto.ArtistTypeAssignmentResponseDto;
import com.pid.backend.entity.Artist;
import com.pid.backend.entity.ArtistType;
import com.pid.backend.entity.ArtistTypeAssignment;
import com.pid.backend.repository.ArtistRepository;
import com.pid.backend.repository.ArtistTypeAssignmentRepository;
import com.pid.backend.repository.ArtistTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistTypeAssignmentService {

    private final ArtistTypeAssignmentRepository artistTypeAssignmentRepository;
    private final ArtistRepository artistRepository;
    private final ArtistTypeRepository artistTypeRepository;

    public List<ArtistTypeAssignmentResponseDto> getAllAssignments() {
        return artistTypeAssignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ArtistTypeAssignmentResponseDto getAssignmentById(Long id) {
        ArtistTypeAssignment assignment = artistTypeAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type assignment not found with id: " + id));

        return mapToResponse(assignment);
    }

    public ArtistTypeAssignmentResponseDto createAssignment(ArtistTypeAssignmentRequestDto requestDto) {
        Artist artist = artistRepository.findById(requestDto.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + requestDto.getArtistId()));

        ArtistType artistType = artistTypeRepository.findById(requestDto.getArtistTypeId())
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + requestDto.getArtistTypeId()));

        ArtistTypeAssignment assignment = ArtistTypeAssignment.builder()
                .artist(artist)
                .artistType(artistType)
                .build();

        return mapToResponse(artistTypeAssignmentRepository.save(assignment));
    }

    public ArtistTypeAssignmentResponseDto updateAssignment(Long id, ArtistTypeAssignmentRequestDto requestDto) {
        ArtistTypeAssignment assignment = artistTypeAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type assignment not found with id: " + id));

        Artist artist = artistRepository.findById(requestDto.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + requestDto.getArtistId()));

        ArtistType artistType = artistTypeRepository.findById(requestDto.getArtistTypeId())
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + requestDto.getArtistTypeId()));

        assignment.setArtist(artist);
        assignment.setArtistType(artistType);

        return mapToResponse(artistTypeAssignmentRepository.save(assignment));
    }

    public void deleteAssignment(Long id) {
        ArtistTypeAssignment assignment = artistTypeAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type assignment not found with id: " + id));

        artistTypeAssignmentRepository.delete(assignment);
    }

    private ArtistTypeAssignmentResponseDto mapToResponse(ArtistTypeAssignment assignment) {
        Artist artist = assignment.getArtist();
        ArtistType artistType = assignment.getArtistType();

        return ArtistTypeAssignmentResponseDto.builder()
                .id(assignment.getId())
                .artistId(artist != null ? artist.getId() : null)
                .artistFirstname(artist != null ? artist.getFirstname() : null)
                .artistLastname(artist != null ? artist.getLastname() : null)
                .artistTypeId(artistType != null ? artistType.getId() : null)
                .artistTypeName(artistType != null ? artistType.getTypeName() : null)
                .build();
    }
}