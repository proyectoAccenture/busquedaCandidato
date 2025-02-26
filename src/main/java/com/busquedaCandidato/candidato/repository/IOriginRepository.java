package com.busquedaCandidato.candidato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busquedaCandidato.candidato.entity.OriginEntity;

public interface IOriginRepository extends JpaRepository<OriginEntity,Long>{
    boolean existsByName(String name);

}
