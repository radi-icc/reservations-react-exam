package com.pid.backend.repository;

import com.pid.backend.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByRepresentationId(Long representationId);
}
