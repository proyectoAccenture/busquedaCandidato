package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.utility.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IPostulationRepository extends JpaRepository<PostulationEntity, Long> {
    PostulationEntity findByCandidateId (Long candidateId);
    List<PostulationEntity> findByRole(RoleEntity role);
    Boolean existsByCandidateId(Long candidateId);
    boolean existsByCandidate_IdAndRole_IdAndStatus(Long candidateId, Long roleId, Status status);

    @Query("SELECT p FROM PostulationEntity p " +
            "JOIN p.candidate c " +
            "WHERE " +
            "(:word1 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word1, '%'))) " +
            "AND (:word2 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word2, '%'))) " +
            "AND (:word3 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word3, '%'))) " +
            "AND (:word4 IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :word4, '%')))")
    List<PostulationEntity> searchByCandidateNameOrLastName(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4);

    @Query("SELECT p FROM PostulationEntity p " +
            "JOIN p.candidate c " +
            "JOIN c.origin o " +
            "JOIN c.jobProfile jp " +
            "JOIN p.role r " +
            "LEFT JOIN p.process pr " +
            "LEFT JOIN pr.candidateState cs " +
            "LEFT JOIN cs.state s " +
            "WHERE (" +
            "(:word1 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word1, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word1, '%'))) AND " +
            "(:word2 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word2, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word2, '%'))) AND " +
            "(:word3 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word3, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word3, '%'))) AND " +
            "(:word4 IS NULL OR LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :word4, '%')) OR LOWER(COALESCE(c.lastName, '')) LIKE LOWER(CONCAT('%', :word4, '%'))) " +
            ") OR " +
            "( :statusQuery IS NOT NULL AND p.status = :statusQuery ) OR " +
            "LOWER(COALESCE(r.nameRole, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(jp.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(o.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(s.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(c.source, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(c.city, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(c.skills, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(COALESCE(c.card, 0) AS string) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PostulationEntity> searchByCandidateNameLastNameAndRole(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("word3") String word3,
            @Param("word4") String word4,
            @Param("query") String query,
            @Param("statusQuery") Boolean statusQuery);

}
