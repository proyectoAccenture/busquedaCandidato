package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Interfaz para mapear JobProfileRequestDto a JobProfileEntity.
 * Utiliza MapStruct para la implementación del mapeo.
 */
@Mapper(componentModel = "spring")
public interface IMapperJobProfileRequest {

    /**
     * Mapea JobProfileRequestDto a JobProfileEntity.
     *
     * @param jobProfileRequestDto El DTO que representa una solicitud de perfil de trabajo.
     * @return La entidad JobProfileEntity mapeada.
     */
    @Mapping(source = "name", target = "name")
    JobProfileEntity  JobProfileResquestToJobProfile(JobProfileRequestDto jobProfileRequestDto);


}
