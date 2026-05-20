package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "artist_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name")
    private String typeName;
}