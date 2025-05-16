package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Long>{
    List<CandidateEntity> findByIdIn(List<Long> candidateIds);
    Optional<CandidateEntity> findByCardAndStatusNot(String card, Status status);
    Optional<CandidateEntity> findByPhoneAndStatusNot(String card, Status status);
    Optional<CandidateEntity> findByEmailAndStatusNot(String card, Status status);
    boolean existsByCardAndStatusNot(String card, Status status);
    boolean existsByPhoneAndStatusNot(String phone, Status status);
    boolean existsByCardAndStatusNotAndIdNot(String card, Status status, Long id);
    boolean existsByPhoneAndStatusNotAndIdNot(String phone, Status status, Long id);


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

    @Modifying
    @Query("UPDATE CandidateEntity c SET c.origin = null WHERE c.origin.id = :originId")
    void detachOriginFromCandidates(@Param("originId") Long originId);

    @Modifying
    @Query("UPDATE CandidateEntity c SET c.jobProfile = null WHERE c.jobProfile.id = :jobProfileId")
    void detachJobProfileFromCandidates(@Param("jobProfileId") Long jobProfileId);

    List<CandidateEntity> findAllByOriginId(Long originId);

    List<CandidateEntity> findAllByJobProfileId(Long jobProfileId);

}
