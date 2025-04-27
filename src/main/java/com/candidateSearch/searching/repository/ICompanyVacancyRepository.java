package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CompanyVacancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyVacancyRepository extends JpaRepository<CompanyVacancyEntity, Long> {
}
