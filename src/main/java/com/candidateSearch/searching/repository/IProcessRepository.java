package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IProcessRepository extends JpaRepository<ProcessEntity, Long> {
    Optional<ProcessEntity> findByPostulationId(Long id);
    List<ProcessEntity> findAllByPostulationId(Long id);
    Page<ProcessEntity> findByStatusIn(List<Status> statuses, Pageable pageable);

    @Query("SELECT p FROM ProcessEntity p " +
            "JOIN p.postulation po " +
            "JOIN po.candidate c " +
            "WHERE " +
            "(:word1 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word1, '%'))) " +
            "AND (:word2 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word2, '%'))) " +
            "AND (:word3 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word3, '%'))) " +
            "AND (:word4 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word4, '%')))")
    List<ProcessEntity> searchByCandidateNameOrLastName2(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4);

    @Query("SELECT p FROM ProcessEntity p " +
            "JOIN p.postulation po " +
            "JOIN po.candidate c " +
            "WHERE (:statuses IS NULL OR po.status IN :statuses) AND " +
            "(:word1 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word1, '%'))) " +
            "AND (:word2 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word2, '%'))) " +
            "AND (:word3 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word3, '%'))) " +
            "AND (:word4 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word4, '%')))")
    Page<ProcessEntity> searchByCandidateNameAndStatuses(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4,
            @Param("statuses") List<Status> statuses,
            Pageable pageable);


}
