package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICandidateStateRepository extends JpaRepository<CandidateStateEntity, Long> {
    Optional<CandidateStateEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);
}
