package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleIDRepository extends JpaRepository<RoleIDEntity, Long> {
    boolean existsByName(String name);
}
