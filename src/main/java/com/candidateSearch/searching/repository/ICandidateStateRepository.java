package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICandidateStateRepository extends JpaRepository<CandidateStateEntity, Long> {
    CandidateStateEntity findByProcessId (Long idProcess);
    Optional<CandidateStateEntity> findTopByProcessOrderByIdDesc(ProcessEntity process);

    @Modifying
    @Query("UPDATE CandidateStateEntity cs SET cs.state = null WHERE cs.state.id = :stateId")
    void detachStateFromCandidateState(@Param("stateId") Long stateId);

    List<CandidateStateEntity> findAllByStateId(Long stateId);
}
