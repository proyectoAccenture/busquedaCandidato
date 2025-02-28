package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IStateRepository  extends JpaRepository<StateEntity, Long> {

    boolean existsByName(String name);

}
