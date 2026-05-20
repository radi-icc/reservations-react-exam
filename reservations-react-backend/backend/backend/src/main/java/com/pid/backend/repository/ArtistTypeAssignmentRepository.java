package com.pid.backend.repository;

import com.pid.backend.entity.ArtistTypeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistTypeAssignmentRepository extends JpaRepository<ArtistTypeAssignment, Long> {
}