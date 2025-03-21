package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IProcessRepository extends JpaRepository<ProcessEntity, Long> {
    Boolean existsByPostulationId(Long phaseId);
    Boolean existsByCandidatePhasesId(Long phaseId);
    Optional<ProcessEntity> findByPostulationId(Long id);
    List<ProcessEntity> findByIdIn(List<Long> processId);

    @Query("SELECT p FROM ProcessEntity p " +
            "JOIN p.postulation po " +
            "JOIN po.candidate c " +
            "WHERE LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ProcessEntity> searchByCandidateNameOrLastName(@Param("query") String query);
}
