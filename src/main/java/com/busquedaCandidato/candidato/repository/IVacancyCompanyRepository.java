package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVacancyCompanyRepository extends JpaRepository<VacancyCompanyEntity, Long> {
}
