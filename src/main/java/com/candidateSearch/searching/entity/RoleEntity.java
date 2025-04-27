package com.candidateSearch.searching.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToOne
    @JoinColumn(name="company_vacancy_id")
    private CompanyVacancyEntity companyVacancy;

    @OneToMany(mappedBy = "role",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<PostulationEntity> postulations;
}
