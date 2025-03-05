package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICandidateProcessRepository extends JpaRepository<CandidateProcessEntity, Long> {
    Optional<CandidateProcessEntity> findTopByProcessOrderByAssignedDateDesc(ProcessEntity process);

}
