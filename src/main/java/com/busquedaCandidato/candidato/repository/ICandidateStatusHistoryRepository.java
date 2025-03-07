package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICandidateStatusHistoryRepository extends JpaRepository<CandidateStatusHistoryEntity, Long> {
}
