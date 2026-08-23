package com.pid.backend.service;

import com.pid.backend.dto.ReservationRequestDto;
import com.pid.backend.dto.ReservationResponseDto;
import com.pid.backend.entity.*;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RepresentationReservationRepository representationReservationRepository;
    private final RepresentationRepository representationRepository;
    private final PriceRepository priceRepository;
    private final CurrentUserService currentUserService;

    public List<ReservationResponseDto> getMyReservations() {
        User user = currentUserService.getCurrentUser();

        return reservationRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapReservationToResponse)
                .toList();
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapReservationToResponse)
                .toList();
    }

    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        User currentUser = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(currentUser)
                && !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot view another user's reservation");
        }

        return mapReservationToResponse(reservation);
    }

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        User user = currentUserService.getCurrentUser();

        Representation representation = representationRepository.findById(requestDto.getRepresentationId())
                .orElseThrow(() -> new ResourceNotFoundException("Representation not found with id: " + requestDto.getRepresentationId()));

        Price price = priceRepository.findById(requestDto.getPriceId())
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + requestDto.getPriceId()));

        Show show = representation.getShow();

        if (show == null || !Boolean.TRUE.equals(show.getBookable())) {
            throw new BadRequestException("This show is not bookable");
        }

        if (representation.getPerformanceDate() != null &&
                representation.getPerformanceDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot reserve a past performance");
        }

        Integer capacity = representation.getCapacity() != null ? representation.getCapacity() : 0;
        Integer bookedSeats = representation.getBookedSeats() != null ? representation.getBookedSeats() : 0;
        Integer requestedSeats = requestDto.getQuantity();

        if (capacity <= 0) {
            throw new BadRequestException("This representation has no available capacity");
        }

        if (Boolean.TRUE.equals(representation.getFull())) {
            throw new BadRequestException("This representation is already full");
        }

        int availableSeats = capacity - bookedSeats;

        if (requestedSeats > availableSeats) {
            throw new BadRequestException("Not enough seats available. Available seats: " + availableSeats);
        }

        BigDecimal totalPrice = price.getAmount().multiply(BigDecimal.valueOf(requestedSeats));

        Reservation reservation = Reservation.builder()
                .user(user)
                .totalPrice(totalPrice)
                .status("CONFIRMED")
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        RepresentationReservation reservationLine = RepresentationReservation.builder()
                .reservation(savedReservation)
                .representation(representation)
                .price(price)
                .quantity(requestedSeats)
                .build();

        RepresentationReservation savedReservationLine =
                representationReservationRepository.save(reservationLine);

        int updatedBookedSeats = bookedSeats + requestedSeats;

        representation.setBookedSeats(updatedBookedSeats);
        representation.setFull(updatedBookedSeats >= capacity);

        representationRepository.save(representation);

        return mapToResponse(savedReservation, savedReservationLine);
    }

    @Transactional
    public void cancelReservation(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot cancel another user's reservation");
        }

        if ("CANCELLED".equalsIgnoreCase(reservation.getStatus())) {
            throw new BadRequestException("Reservation is already cancelled");
        }

        List<RepresentationReservation> lines =
                representationReservationRepository.findByReservationId(reservation.getId());

        for (RepresentationReservation line : lines) {
            Representation representation = line.getRepresentation();

            int bookedSeats = representation.getBookedSeats() != null ? representation.getBookedSeats() : 0;
            int quantity = line.getQuantity() != null ? line.getQuantity() : 0;

            representation.setBookedSeats(Math.max(0, bookedSeats - quantity));
            representation.setFull(false);

            representationRepository.save(representation);
        }

        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    private ReservationResponseDto mapReservationToResponse(Reservation reservation) {
        List<RepresentationReservation> lines =
                representationReservationRepository.findByReservationId(reservation.getId());

        if (lines.isEmpty()) {
            User user = reservation.getUser();

            return ReservationResponseDto.builder()
                    .reservationId(reservation.getId())
                    .userId(user != null ? user.getId() : null)
                    .username(user != null ? user.getUsername() : null)
                    .email(user != null ? user.getEmail() : null)
                    .totalPrice(reservation.getTotalPrice())
                    .status(reservation.getStatus())
                    .reservationDate(reservation.getReservationDate())
                    .build();
        }

        return mapToResponse(reservation, lines.get(0));
    }

    private ReservationResponseDto mapToResponse(
            Reservation reservation,
            RepresentationReservation line
    ) {
        User user = reservation.getUser();
        Representation representation = line.getRepresentation();
        Price price = line.getPrice();

        return ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .userId(user != null ? user.getId() : null)
                .username(user != null ? user.getUsername() : null)
                .email(user != null ? user.getEmail() : null)
                .representationId(representation != null ? representation.getId() : null)
                .showTitle(
                        representation != null && representation.getShow() != null
                                ? representation.getShow().getTitle()
                                : null
                )
                .performanceDate(representation != null ? representation.getPerformanceDate() : null)
                .performanceTime(representation != null ? representation.getPerformanceTime() : null)
                .priceId(price != null ? price.getId() : null)
                .priceLabel(price != null ? price.getLabel() : null)
                .unitPrice(price != null ? price.getAmount() : null)
                .quantity(line.getQuantity())
                .totalPrice(reservation.getTotalPrice())
                .status(reservation.getStatus())
                .reservationDate(reservation.getReservationDate())
                .build();
    }
}
