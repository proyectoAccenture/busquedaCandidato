package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostulationRepository extends JpaRepository<PostulationEntity, Long> {

}
