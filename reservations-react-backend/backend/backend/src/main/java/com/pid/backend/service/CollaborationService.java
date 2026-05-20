package com.pid.backend.service;

import com.pid.backend.dto.CollaborationRequestDto;
import com.pid.backend.dto.CollaborationResponseDto;
import com.pid.backend.entity.Artist;
import com.pid.backend.entity.ArtistType;
import com.pid.backend.entity.ArtistTypeAssignment;
import com.pid.backend.entity.Collaboration;
import com.pid.backend.entity.Show;
import com.pid.backend.repository.ArtistTypeAssignmentRepository;
import com.pid.backend.repository.CollaborationRepository;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationService {

    private final CollaborationRepository collaborationRepository;
    private final ArtistTypeAssignmentRepository artistTypeAssignmentRepository;
    private final ShowRepository showRepository;

    public List<CollaborationResponseDto> getAllCollaborations() {
        return collaborationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CollaborationResponseDto getCollaborationById(Long id) {
        Collaboration collaboration = collaborationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collaboration not found with id: " + id));

        return mapToResponse(collaboration);
    }

    public CollaborationResponseDto createCollaboration(CollaborationRequestDto requestDto) {
        ArtistTypeAssignment assignment = artistTypeAssignmentRepository.findById(requestDto.getArtistTypeAssignmentId())
                .orElseThrow(() -> new RuntimeException("Artist type assignment not found with id: " + requestDto.getArtistTypeAssignmentId()));

        Show show = showRepository.findById(requestDto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + requestDto.getShowId()));

        Collaboration collaboration = Collaboration.builder()
                .artistTypeAssignment(assignment)
                .show(show)
                .build();

        return mapToResponse(collaborationRepository.save(collaboration));
    }

    public CollaborationResponseDto updateCollaboration(Long id, CollaborationRequestDto requestDto) {
        Collaboration collaboration = collaborationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collaboration not found with id: " + id));

        ArtistTypeAssignment assignment = artistTypeAssignmentRepository.findById(requestDto.getArtistTypeAssignmentId())
                .orElseThrow(() -> new RuntimeException("Artist type assignment not found with id: " + requestDto.getArtistTypeAssignmentId()));

        Show show = showRepository.findById(requestDto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + requestDto.getShowId()));

        collaboration.setArtistTypeAssignment(assignment);
        collaboration.setShow(show);

        return mapToResponse(collaborationRepository.save(collaboration));
    }

    public void deleteCollaboration(Long id) {
        Collaboration collaboration = collaborationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collaboration not found with id: " + id));

        collaborationRepository.delete(collaboration);
    }

    private CollaborationResponseDto mapToResponse(Collaboration collaboration) {
        Show show = collaboration.getShow();
        ArtistTypeAssignment assignment = collaboration.getArtistTypeAssignment();

        Artist artist = assignment != null ? assignment.getArtist() : null;
        ArtistType artistType = assignment != null ? assignment.getArtistType() : null;

        return CollaborationResponseDto.builder()
                .id(collaboration.getId())
                .showId(show != null ? show.getId() : null)
                .showTitle(show != null ? show.getTitle() : null)
                .artistId(artist != null ? artist.getId() : null)
                .artistFirstname(artist != null ? artist.getFirstname() : null)
                .artistLastname(artist != null ? artist.getLastname() : null)
                .artistTypeId(artistType != null ? artistType.getId() : null)
                .artistTypeName(artistType != null ? artistType.getTypeName() : null)
                .build();
    }
}