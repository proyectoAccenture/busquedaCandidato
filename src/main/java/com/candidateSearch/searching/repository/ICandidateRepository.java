package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Long>{
    List<CandidateEntity> findByIdIn(List<Long> candidateIds);
    CandidateEntity findByPostulation(PostulationEntity postulation);
    Optional<CandidateEntity> findByCardAndStatusNot(String card, Status status);
    Optional<CandidateEntity> findByPhoneAndStatusNot(String card, Status status);
    Optional<CandidateEntity> findByEmailAndStatusNot(String card, Status status);
    Page<CandidateEntity> findByStatusIn(List<Status> statuses, Pageable pageable);

    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.card) LIKE CONCAT('%', :query, '%') " +
            "OR LOWER(c.phone) LIKE CONCAT('%', :query, '%') " +
            "OR LOWER(c.city) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR CAST(c.birthdate AS string) LIKE CONCAT('%', :query, '%') " +
            "OR CAST(c.datePresentation AS string) LIKE CONCAT('%', :query, '%')")
    Page<CandidateEntity> searchCandidates(@Param("query") String query, Pageable pageable);

    @Query("""
    SELECT c FROM CandidateEntity c
    LEFT JOIN c.origin o
    LEFT JOIN c.jobProfile jp
    WHERE (
        (:word1 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word1, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word1, '%'))) AND
        (:word2 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word2, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word2, '%'))) AND
        (:word3 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word3, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word3, '%'))) AND
        (:word4 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word4, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word4, '%')))
    )
    AND c.status IN :statuses
    OR LOWER(COALESCE(c.card, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.phone, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.city, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.email, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.source, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.skills, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.workExperience, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.seniority, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(c.yearsExperience, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(o.name, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(COALESCE(jp.name, '')) LIKE LOWER(CONCAT('%', :query, '%'))
    OR CAST(COALESCE(c.birthdate, null) AS string) LIKE LOWER(CONCAT('%', :query, '%'))
    OR CAST(COALESCE(c.datePresentation, null) AS string) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(CONCAT(c.lastName, ' ', c.name)) LIKE LOWER(CONCAT('%', :query, '%'))
""")
    Page<CandidateEntity> searchCandidatesV2(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4,
            @Param("query") String query,
            @Param("statuses") List<Status> statuses,
            Pageable pageable
    );




    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query1, '%')) " +
            "AND LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query2, '%'))")
    List<CandidateEntity> searchByPartialName(
            @Param("query1") String query1,
            @Param("query2") String query2,
            Pageable pageable);

    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) ")
    List<CandidateEntity> searchByFullName(@Param("query") String query, Pageable pageable);


    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE c.status IN :validStatuses AND " +
            "LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query1, '%')) " +
            "AND LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query2, '%'))")
    Page<CandidateEntity> searchByPartialNameV2(
            @Param("query1") String query1,
            @Param("query2") String query2,
            @Param("validStatuses") List<Status> validStatuses,
            Pageable pageable);

    @Query("SELECT c FROM CandidateEntity c " +
            "WHERE c.status IN :validStatuses AND " +
            "LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<CandidateEntity> searchByFullNameV2(
            @Param("query") String query,
            @Param("validStatuses") List<Status> validStatuses,
            Pageable pageable);



}
