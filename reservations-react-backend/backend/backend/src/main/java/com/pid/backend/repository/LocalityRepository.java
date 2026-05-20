package com.pid.backend.repository;

import com.pid.backend.entity.Locality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalityRepository extends JpaRepository<Locality, Long> {
}