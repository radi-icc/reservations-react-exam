package com.pid.backend.repository;

import com.pid.backend.entity.RepresentationReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepresentationReservationRepository extends JpaRepository<RepresentationReservation, Long> {

    List<RepresentationReservation> findByReservationId(Long reservationId);

    List<RepresentationReservation> findByReservationUserId(Long userId);

    List<RepresentationReservation> findByRepresentationShowId(Long showId);
}