package com.busquedaCandidato.candidato.repository;


import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostulationRepository extends JpaRepository<PostulationEntity, Long> {
    List<PostulationEntity> findByVacancyCompanyIdIn(List<Long> vacancyIds);
    Boolean existsByCandidateId(Long candidateId);
    Boolean existsByVacancyCompanyId(Long vacancyCompany);
    boolean existsByCandidate_IdAndVacancyCompany_IdAndStatus(Long candidateId, Long vacancyCompanyId, Boolean status);

    @Query("SELECT p FROM PostulationEntity p " +
            "JOIN p.candidate c " +
            "WHERE " +
            "(:word1 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word1, '%'))) " +
            "AND (:word2 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word2, '%'))) " +
            "AND (:word3 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word3, '%'))) " +
            "AND (:word4 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word4, '%'))) ")
    List<PostulationEntity> searchByCandidateNameOrLastName(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4);

    @Query("SELECT p FROM PostulationEntity p " +
            "JOIN p.candidate c " +
            "JOIN p.vacancyCompany v " +
            "JOIN v.role r " +
            "WHERE (" +
            "(:word1 IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :word1, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :word1, '%'))) " +
            "AND (:word2 IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :word2, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :word2, '%'))) " +
            "AND (:word3 IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :word3, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :word3, '%'))) " +
            "AND (:word4 IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :word4, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :word4, '%'))) " +
            "OR LOWER(r.name) LIKE LOWER(CONCAT('%', :roleId, '%')))")
    List<PostulationEntity> searchByCandidateNameLastNameAndRole(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4,
            @Param("roleId") String roleId);





}
