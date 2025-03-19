package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Long> {
    boolean existsByCard(Long card);
    boolean existsByPhone(Long phone);
    List<CandidateEntity> findByName(String name);
    List<CandidateEntity> findByIdIn(List<Long> candidateIds);
}
