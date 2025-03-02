package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleIDRepository extends JpaRepository<RoleIDEntity, Long> {


    /**
     * Comprueba si una entidad RoleIDEntity con el nombre especificado existe.
     *
     * @param name El nombre del Role ID a buscar.
     * @return true si una entidad RoleIDEntity con el nombre especificado existe, de lo contrario false.
     */
    boolean existsByName(String name);
}
