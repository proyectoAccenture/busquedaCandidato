package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CompanyVacancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyVacancyRepository extends JpaRepository<CompanyVacancyEntity, Long> {
    Boolean existsByJobProfileId(Long jobProfileId);
    Boolean existsByOriginId(Long originId);
}
