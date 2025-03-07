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
<<<<<<< HEAD
@Table(name = "postulation")
=======
@Table(name = "Postulation")
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594
public class PostulationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String salaryAspiration;

    @Column(nullable = false)
    private LocalDate datePresentation;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @ManyToOne
    @JoinColumn(name = "company_vacancy_id", nullable = false)
    private VacancyCompanyEntity  vacancyCompany;
}
