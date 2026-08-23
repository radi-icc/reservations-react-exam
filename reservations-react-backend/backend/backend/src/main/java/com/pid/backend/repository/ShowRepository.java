package com.pid.backend.repository;

import com.pid.backend.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByProducerId(Long producerId);

    @Query("""
            SELECT s FROM Show s
            WHERE (:search IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:locationId IS NULL OR s.location.id = :locationId)
            AND (:bookable IS NULL OR s.bookable = :bookable)
            """)
    Page<Show> searchShows(
            @Param("search") String search,
            @Param("locationId") Long locationId,
            @Param("bookable") Boolean bookable,
            Pageable pageable
    );
}
