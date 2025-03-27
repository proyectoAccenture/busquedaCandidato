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
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private Long card;

    @Column(unique = true, nullable = false)
    private Long phone;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    private String yearsExperience;

    @Column(nullable = false)
    private String workExperience;

    @Column(nullable = false)
    private String seniority;

    @Column(nullable = false)
    private Long salaryAspiration;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private LocalDate datePresentation;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private OriginEntity origin;

    @ManyToOne
    @JoinColumn(name = "job_profile_id", nullable = false)
    private JobProfileEntity jobProfile;
}