package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostulationRepository extends JpaRepository<PostulationEntity, Long> {
    List<PostulationEntity> findByVacancyCompanyIdIn(List<Long> vacancyIds);
    Boolean existsByCandidateId(Long candidateId);
    Boolean existsByVacancyCompanyId(Long vacancyCompany);
    List<PostulationEntity> findByCandidate(CandidateEntity candidate);

    boolean existsByCandidate_IdAndVacancyCompany_IdAndStatus(Long candidateId, Long vacancyCompanyId, Boolean status);

    @Query("SELECT p FROM PostulationEntity p " +
            "JOIN p.candidate c " +
            "WHERE LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PostulationEntity> searchByCandidateNameOrLastName(@Param("query") String query);
}
