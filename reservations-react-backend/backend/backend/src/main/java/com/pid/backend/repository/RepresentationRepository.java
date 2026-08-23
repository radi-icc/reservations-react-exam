package com.pid.backend.repository;

import com.pid.backend.entity.Representation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepresentationRepository extends JpaRepository<Representation, Long> {
    List<Representation> findByShowId(Long showId);
}
