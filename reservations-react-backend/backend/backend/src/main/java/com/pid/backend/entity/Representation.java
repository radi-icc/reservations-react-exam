package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "representations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Representation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "performance_date")
    private LocalDate performanceDate;

    @Column(name = "performance_time")
    private LocalTime performanceTime;

    private Integer capacity;

    @Column(name = "booked_seats")
    private Integer bookedSeats;

    @Column(name = "is_full")
    private Boolean full;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @PrePersist
    public void onCreate() {
        if (capacity == null) {
            capacity = 0;
        }

        if (bookedSeats == null) {
            bookedSeats = 0;
        }

        if (full == null) {
            full = false;
        }
    }
}