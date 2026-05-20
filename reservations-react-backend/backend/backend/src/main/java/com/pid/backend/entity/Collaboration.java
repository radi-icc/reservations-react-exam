package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "collaborations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collaboration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_type_id", nullable = false)
    private ArtistTypeAssignment artistTypeAssignment;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;
}