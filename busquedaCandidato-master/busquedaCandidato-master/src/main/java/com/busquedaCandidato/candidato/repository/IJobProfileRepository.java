package com.busquedaCandidato.candidato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;


public interface IJobProfileRepository extends JpaRepository<JobProfileEntity, Long> {

    boolean existsByName(String name); 

}
