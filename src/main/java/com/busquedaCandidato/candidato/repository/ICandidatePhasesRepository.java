package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICandidatePhasesRepository extends JpaRepository<CandidatePhasesEntity, Long> {
    Optional<CandidatePhasesEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);
}
