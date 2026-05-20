package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "localities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postal_code", unique = true)
    private String postalCode;

    @Column(name = "locality", unique = true)
    private String locality;
}