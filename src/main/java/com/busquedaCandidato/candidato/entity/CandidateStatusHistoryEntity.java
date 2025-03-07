package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.security.Timestamp;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "candidate_status_history")
public class CandidateStatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    @ManyToOne
    @JoinColumn(name = "postulation_id", nullable = false)
    private PostulationEntity postulation;

    @ManyToOne
    @JoinColumn(name = "candidate_phases_id", nullable = false)
    private CandidatePhasesEntity candidatePhases;
}
