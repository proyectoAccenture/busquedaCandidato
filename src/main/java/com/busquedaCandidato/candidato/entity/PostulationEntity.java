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
@Table(name = "postulation")
public class PostulationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long salaryAspiration;

    @Column(nullable = false)
    private LocalDate datePresentation;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @ManyToOne
    @JoinColumn(name = "company_vacancy_id", nullable = false)
    private VacancyCompanyEntity  vacancyCompany;

    @OneToOne(mappedBy = "postulation")
    private ProcessEntity process;

}
