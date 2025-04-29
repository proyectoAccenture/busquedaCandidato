package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.JobProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobProfileRepository  extends JpaRepository<JobProfileEntity, Long> {
    boolean existsByName(String name);
}
