package com.pid.backend.repository;

import com.pid.backend.entity.ArtistType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistTypeRepository extends JpaRepository<ArtistType, Long> {
}