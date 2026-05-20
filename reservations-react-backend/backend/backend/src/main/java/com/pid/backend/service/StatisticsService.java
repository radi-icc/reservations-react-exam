package com.pid.backend.service;

import com.pid.backend.dto.GeneralStatisticsResponseDto;
import com.pid.backend.dto.ShowSalesStatsResponseDto;
import com.pid.backend.entity.RepresentationReservation;
import com.pid.backend.entity.Reservation;
import com.pid.backend.entity.Show;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ShowRepository showRepository;
    private final RepresentationRepository representationRepository;
    private final ReservationRepository reservationRepository;
    private final RepresentationReservationRepository representationReservationRepository;
    private final UserRepository userRepository;

    public ShowSalesStatsResponseDto getShowSalesStats(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + showId));

        List<RepresentationReservation> lines =
                representationReservationRepository.findByRepresentationShowId(showId);

        int totalSeatsSold = lines.stream()
                .filter(this::isConfirmedReservationLine)
                .mapToInt(line -> line.getQuantity() != null ? line.getQuantity() : 0)
                .sum();

        BigDecimal totalRevenue = lines.stream()
                .filter(this::isConfirmedReservationLine)
                .map(line -> {
                    BigDecimal amount = line.getPrice() != null && line.getPrice().getAmount() != null
                            ? line.getPrice().getAmount()
                            : BigDecimal.ZERO;

                    int quantity = line.getQuantity() != null ? line.getQuantity() : 0;

                    return amount.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalReservations = lines.stream()
                .filter(this::isConfirmedReservationLine)
                .map(line -> line.getReservation().getId())
                .distinct()
                .count();

        return ShowSalesStatsResponseDto.builder()
                .showId(show.getId())
                .showTitle(show.getTitle())
                .totalReservations((int) totalReservations)
                .totalSeatsSold(totalSeatsSold)
                .totalRevenue(totalRevenue)
                .build();
    }

    public GeneralStatisticsResponseDto getGeneralStatistics() {
        BigDecimal totalRevenue = reservationRepository.findAll()
                .stream()
                .filter(reservation -> "CONFIRMED".equalsIgnoreCase(reservation.getStatus()))
                .map(Reservation::getTotalPrice)
                .filter(totalPrice -> totalPrice != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return GeneralStatisticsResponseDto.builder()
                .totalShows(showRepository.count())
                .totalRepresentations(representationRepository.count())
                .totalReservations(reservationRepository.count())
                .totalUsers(userRepository.count())
                .totalRevenue(totalRevenue)
                .build();
    }

    private boolean isConfirmedReservationLine(RepresentationReservation line) {
        return line.getReservation() != null
                && "CONFIRMED".equalsIgnoreCase(line.getReservation().getStatus());
    }
}