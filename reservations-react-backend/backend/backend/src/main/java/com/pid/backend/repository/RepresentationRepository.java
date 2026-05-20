package com.pid.backend.repository;

import com.pid.backend.entity.Representation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepresentationRepository extends JpaRepository<Representation, Long> {
}