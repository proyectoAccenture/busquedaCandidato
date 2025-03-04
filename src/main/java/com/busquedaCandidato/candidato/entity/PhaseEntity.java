package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "Fase")
public class PhaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // Se relaciona con los procesos en la tabla intermedia
    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
    private List<CandidateProcessEntity> processPhases;
}
