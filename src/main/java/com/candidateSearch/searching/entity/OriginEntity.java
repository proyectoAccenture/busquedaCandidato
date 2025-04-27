package com.candidateSearch.searching.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "origin")
public class OriginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CompanyVacancyEntity> vacancies = new ArrayList<>();;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CandidateEntity> candidates = new ArrayList<>();;
}
