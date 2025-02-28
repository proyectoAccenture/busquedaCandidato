package com.busquedaCandidato.candidato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;

public interface IRoleIDRepository extends JpaRepository<RoleIDEntity, Long> {


    /**
     * Comprueba si una entidad RolIDEntity con el nombre especificado existe.
     *
     * @param name El nombre del role ID a buscar.
     * @return true si una entidad RolIDEntity con el nombre especificado existe, de lo contrario false.
     */
    boolean existsByName(String name);
}
