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
@Table(name = "VacancyCompany")
public class VacancyCompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contract;

    @Column(nullable = false)
    private String salary;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate datePublication;

    @Column(nullable = false)
    private String source;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private RoleIDEntity role;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private JobProfileEntity jobProfile;
}
