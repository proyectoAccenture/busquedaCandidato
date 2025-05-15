

package com.busquedaCandidato.candidato.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busquedaCandidato.candidato.entity.CandidateEntity;


@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {

    Optional<CandidateEntity> findfindByname(Long name);

    Optional<CandidateEntity> findfindByIDCard(Long IDCard);
    long countByIDCard (Long IDCard);
    


}


