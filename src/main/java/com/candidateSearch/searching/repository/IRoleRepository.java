package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByNameRole(String name);
    boolean existsByNameRoleAndStatusNot(String nameRole, Status status);
    List<RoleEntity> findAllByOriginId(Long originId);
    List<RoleEntity> findAllByJobProfileId(Long jobProfileId);
}
