package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.ProcessEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProcessRepository extends JpaRepository<ProcessEntity, Long> {
    List<ProcessEntity> findByPostulationId(Long id);

    @Query("SELECT p FROM ProcessEntity p " +
            "JOIN p.postulation po " +
            "JOIN po.candidate c " +
            "WHERE LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ProcessEntity> searchByCandidateNameOrLastName(@Param("query") String query);

}
