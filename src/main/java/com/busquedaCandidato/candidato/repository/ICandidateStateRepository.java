package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ICandidateStateRepository extends JpaRepository<CandidateStateEntity, Long> {
    Optional<CandidateStateEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);
    Boolean existsByStateId(Long stateId);

    @Query("SELECT COUNT(c) > 0 FROM CandidateStateEntity c " +
            "WHERE c.process = :process " +
            "AND c.process.postulation = :postulation")
    boolean existsByCandidateAndPostulationAndProcess(CandidateEntity candidate, PostulationEntity postulation, ProcessEntity process);
}
