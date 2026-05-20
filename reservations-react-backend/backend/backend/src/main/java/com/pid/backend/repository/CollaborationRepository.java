package com.pid.backend.repository;

import com.pid.backend.entity.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {
}