package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "company_vacancy")
public class VacancyCompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String contract;

    @Column(nullable = false)
    private Long salary;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private String seniority;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String assignmentTime;

    @ManyToOne
    @JoinColumn(name = "job_profile_id", nullable = false)
    private JobProfileEntity jobProfile;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private OriginEntity origin;
}
