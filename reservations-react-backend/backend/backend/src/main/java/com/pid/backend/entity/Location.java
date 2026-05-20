package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slug;

    private String designation;

    private String address;

    private String website;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "locality_id")
    private Locality locality;
}