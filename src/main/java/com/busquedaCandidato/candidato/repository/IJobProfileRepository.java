package com.busquedaCandidato.candidato.repository;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobProfileRepository  extends JpaRepository<JobProfileEntity, Long> {


    /**
     * Comprueba si una entidad JobProfileEntity con el nombre especificado existe.
     *
     * @param name El nombre del perfil de trabajo a buscar.
     * @return true si una entidad JobProfileEntity con el nombre especificado existe, de lo contrario false.
     */
    boolean existsByName(String name);
}
