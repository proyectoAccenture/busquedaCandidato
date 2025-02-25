package com.busquedaCandidato.candidato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busquedaCandidato.candidato.entity.PhaseEntity;

public interface IPhaseRepository extends JpaRepository<PhaseEntity,Long> {

}