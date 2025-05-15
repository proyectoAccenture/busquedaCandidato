package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vacante")
public class VacancyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private CandidateEntity candidate;

    @OneToOne
    @JoinColumn(name = "jobProfile_id")
    private JobProfileEntity jobProfile;

    @OneToOne
    @JoinColumn(name = "roleID")
    private RoleIDEntity roleID;

    @Column(nullable = false)
    private Date presentationDate;

    @Column(nullable = false)
    private String currentSalary;

    @Column(nullable = false)
    private String salaryAspiration;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private List<String> skill;

    @Column(nullable = false)
    private String contract;
}
