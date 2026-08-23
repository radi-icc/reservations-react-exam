package com.pid.backend.repository;

import com.pid.backend.entity.Representation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface RepresentationRepository extends JpaRepository<Representation, Long> {

    Optional<Representation> findFirstByShowIdAndPerformanceDateGreaterThanEqualOrderByPerformanceDateAscPerformanceTimeAsc(Long showId, LocalDate date);
    List<Representation> findByShowId(Long showId);
}
