package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.OriginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOriginRepository extends JpaRepository<OriginEntity,Long>{
    boolean existsByName(String name);
}
