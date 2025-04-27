package com.candidateSearch.searching.repository;

import com.candidateSearch.searching.entity.OriginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOriginRepository extends JpaRepository<OriginEntity,Long>{
    boolean existsByName(String name);
}
