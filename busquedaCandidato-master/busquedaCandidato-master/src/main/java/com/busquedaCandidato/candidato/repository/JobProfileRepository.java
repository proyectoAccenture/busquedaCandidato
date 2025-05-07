package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface JobProfileRepository extends JpaRepository<JobProfileEntity, Long> {

}
