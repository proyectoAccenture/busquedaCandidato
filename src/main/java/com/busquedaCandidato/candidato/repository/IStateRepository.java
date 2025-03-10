package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStateRepository  extends JpaRepository<StateEntity, Long> {

    boolean existsByName(String name);
    Optional<StateEntity> findByName(String name);

}
