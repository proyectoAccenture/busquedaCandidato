package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.CandidateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Long>{
    boolean existsByCard(Long card);
    boolean existsByPhone(Long phone);
    List<CandidateEntity> findByName(String name);
    List<CandidateEntity> findByIdIn(List<Long> candidateIds);

    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR CAST(c.card AS string) LIKE CONCAT('%', :query, '%') " +
            "OR CAST(c.phone AS string) LIKE CONCAT('%', :query, '%') " +
            "OR LOWER(c.city) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR CAST(c.birthdate AS string) LIKE CONCAT('%', :query, '%') " +
            "OR CAST(c.datePresentation AS string) LIKE CONCAT('%', :query, '%')")
    Page<CandidateEntity> searchCandidates(@Param("query") String query, Pageable pageable);

    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<CandidateEntity> searchByNameOrLastName(@Param("query") String query, Pageable pageable);
}
