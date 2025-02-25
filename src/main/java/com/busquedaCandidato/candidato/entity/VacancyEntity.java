package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Vacante")
public class VacancyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jobProfile_id", nullable = false)
    private JobProfileEntity jobProfile;

    @ManyToOne
    @JoinColumn(name = "roleID_id", nullable = false)
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

    @ElementCollection
    @CollectionTable(name = "Vacancy_Skills", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Column(nullable = false)
    private List<String> skills;

    @Column(nullable = false)
    private String contract;
}
