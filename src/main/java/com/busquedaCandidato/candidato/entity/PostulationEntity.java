package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Postulacion")
public class PostulationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datePresentation;

    @Column(nullable = false)
    private String salaryAspiration;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private VacancyCompanyEntity vacancyCompany;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private CandidateEntity candidate;
}
