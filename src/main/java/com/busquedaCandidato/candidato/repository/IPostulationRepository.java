package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IPostulationRepository extends JpaRepository<PostulationEntity, Long> {
    List<PostulationEntity> findByVacancyCompanyIdIn(List<Long> vacancyIds);
    Boolean existsByCandidateId(Long candidateId);
    Boolean existsByCompanyVacancyId(Long companyVacancyId);
}
