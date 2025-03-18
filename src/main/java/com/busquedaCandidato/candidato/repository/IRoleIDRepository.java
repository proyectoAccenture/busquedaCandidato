package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleIDRepository extends JpaRepository<RoleIDEntity, Long> {
    boolean existsByName(String name);
    Optional<RoleIDEntity> findByName(String name);
}
