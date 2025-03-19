package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICandidatePhasesRepository extends JpaRepository<CandidatePhasesEntity, Long> {
    Optional<CandidatePhasesEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);
    @Query("SELECT COUNT(c) > 0 FROM CandidatePhasesEntity c " +
            "WHERE c.process = :process " +
            "AND c.process.postulation = :postulation")
    boolean existsByCandidateAndPostulationAndProcess(CandidateEntity candidate, PostulationEntity postulation, ProcessEntity process);
}
