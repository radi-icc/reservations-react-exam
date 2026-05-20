package com.pid.backend;

import com.pid.backend.entity.*;
import com.pid.backend.repository.*;
import com.pid.backend.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private RepresentationRepository representationRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RepresentationReservationRepository representationReservationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void getShowSalesStatsShouldCalculateRevenueAndSeats() {
        Show show = Show.builder()
                .id(1L)
                .title("Ayiti")
                .build();

        Reservation reservation = Reservation.builder()
                .id(1L)
                .status("CONFIRMED")
                .build();

        Price price = Price.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(25))
                .build();

        Representation representation = Representation.builder()
                .id(1L)
                .show(show)
                .build();

        RepresentationReservation line = RepresentationReservation.builder()
                .id(1L)
                .reservation(reservation)
                .representation(representation)
                .price(price)
                .quantity(2)
                .build();

        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(representationReservationRepository.findByRepresentationShowId(1L))
                .thenReturn(List.of(line));

        var response = statisticsService.getShowSalesStats(1L);

        assertEquals(2, response.getTotalSeatsSold());
        assertEquals(BigDecimal.valueOf(50), response.getTotalRevenue());
        assertEquals(1, response.getTotalReservations());
    }
}