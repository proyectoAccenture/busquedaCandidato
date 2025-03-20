package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProcessRepository extends JpaRepository<ProcessEntity, Long> {
    Boolean existsByPostulationId(Long phaseId);
    Boolean existsByCandidatePhasesId(Long phaseId);
}
