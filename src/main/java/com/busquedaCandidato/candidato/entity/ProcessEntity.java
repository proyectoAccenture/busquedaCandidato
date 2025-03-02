package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Proceso")
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    // Relación con la tabla intermedia que almacena las fases y sus estados
    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    private List<CandidateProcessEntity> processPhases;
}
