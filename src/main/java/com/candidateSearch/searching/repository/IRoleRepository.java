package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByNameRole(String name);
    boolean existsByNameRoleAndStatusNot(String nameRole, Status status);
    List<RoleEntity> findAllByOriginId(Long originId);
    List<RoleEntity> findAllByJobProfileId(Long jobProfileId);
    Page<RoleEntity> findByStatusIn(List<Status> statuses, Pageable pageable);

        @Query("""
        SELECT r FROM RoleEntity r
        WHERE
            (:query IS NULL OR LOWER(CAST(r.id AS string)) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.nameRole, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.description, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.contract, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(CAST(r.salary AS string)) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(CAST(r.level AS string)) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.seniority, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.skills, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.experience, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(CAST(r.assignmentTime AS string)) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.jobProfile.name, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(r.origin.name, '')) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            AND (r.status IN :statuses)
    """)
        Page<RoleEntity> searchByAllFields(
                @Param("query") String query,
                @Param("statuses") List<Status> statuses,
                Pageable pageable
        );
}
