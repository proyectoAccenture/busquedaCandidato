package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobProfileRepository  extends JpaRepository<JobProfileEntity, Long> {
    boolean existsByName(String name);
}
