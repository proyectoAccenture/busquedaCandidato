package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.StateEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IStateRepository  extends JpaRepository<StateEntity, Long> {

    boolean existsByName(String name);
    Optional<StateEntity> findByName(String name);

}
