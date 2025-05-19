package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ICandidateStateRepository extends JpaRepository<CandidateStateEntity, Long> {
    CandidateStateEntity findByProcessId (Long idProcess);
    Optional<CandidateStateEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);
    List<CandidateStateEntity> findAllByStateId(Long stateId);
}
