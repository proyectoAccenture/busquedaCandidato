package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IVacancyCompanyRepository extends JpaRepository<VacancyCompanyEntity, Long> {
    Boolean existsByJobProfileId(Long jobProfileId);
    Boolean existsByOriginId(Long originId);
}
