package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void onCreate() {
        if (reservationDate == null) {
            reservationDate = LocalDateTime.now();
        }

        if (status == null) {
            status = "PENDING";
        }
    }
}