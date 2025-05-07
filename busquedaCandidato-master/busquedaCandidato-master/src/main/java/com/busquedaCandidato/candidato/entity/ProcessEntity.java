package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.boot.jaxb.Origin;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Proceso")
public class ProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "candidate_id")
    private CandidateEntity candidate;

    @OneToOne
    @JoinColumn(name = "phase_id")
    private PhaseEntity phase;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String description;

    @OneToOne
    @JoinColumn(name = "state_id")
    private StateEntity stateEntity;

    // Status that verifies wheter the candidate passed
    @Column(nullable = false)
    private Boolean state;
}
