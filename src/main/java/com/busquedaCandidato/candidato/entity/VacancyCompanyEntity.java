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
@Table(name = "company_vacancy")
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
<<<<<<< HEAD
    @JoinColumn(name = "role_id", nullable = false)
=======
    @JoinColumn(name = "RoleID_id", nullable = false)
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594
    private RoleIDEntity role;

    @ManyToOne
    @JoinColumn(name = "job_profile_id", nullable = false)
    private JobProfileEntity jobProfile;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private OriginEntity origin;
}
