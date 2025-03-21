package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.PhaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPhaseRepository extends JpaRepository<PhaseEntity,Long> {
    boolean existsByName(String name);
}
