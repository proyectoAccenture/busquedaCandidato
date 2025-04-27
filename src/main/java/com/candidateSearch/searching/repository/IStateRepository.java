package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStateRepository  extends JpaRepository<StateEntity, Long> {
    boolean existsByName(String name);
}
