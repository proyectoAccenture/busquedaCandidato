package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ICandidateStateRepository extends JpaRepository<CandidateStateEntity, Long> {
    List<CandidateStateEntity> findByProcessId (Long idProcess);
    Optional<CandidateStateEntity> findTopByProcessAndStatusHistoryOrderByIdDesc(ProcessEntity process, Status statusHistory);
    List<CandidateStateEntity> findAllByStateId(Long stateId);
}
