package com.pid.backend.controller;

import com.pid.backend.dto.ReservationRequestDto;
import com.pid.backend.dto.ReservationResponseDto;
import com.pid.backend.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationResponseDto> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/me")
    public List<ReservationResponseDto> getMyReservations() {
        return reservationService.getMyReservations();
    }

    @GetMapping("/{id}")
    public ReservationResponseDto getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto createReservation(
            @Valid @RequestBody ReservationRequestDto requestDto
    ) {
        return reservationService.createReservation(requestDto);
    }

    @PatchMapping("/{id}/cancel")
    public void cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }
}