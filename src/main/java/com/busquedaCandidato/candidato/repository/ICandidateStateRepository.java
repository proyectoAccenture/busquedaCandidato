package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateStateEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICandidateStateRepository extends JpaRepository<CandidateStateEntity, Long> {
    Optional<CandidateStateEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);
}
