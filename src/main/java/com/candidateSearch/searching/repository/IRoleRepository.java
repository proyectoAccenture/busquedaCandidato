package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByNameRole(String name);
    boolean existsByNameRole(String roleName);
}
