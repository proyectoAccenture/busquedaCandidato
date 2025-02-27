package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Interfaz para mapear JobProfileEntity a JobProfileResponseDto.
 * Utiliza MapStruct para la implementación del mapeo.
 */
@Mapper(componentModel = "spring")
public interface IMapperJobProfileResponse {


    /**
     * Mapea JobProfileEntity a JobProfileResponseDto.
     *
     * @param jobProfileEntity La entidad que representa un perfil de trabajo.
     * @return El DTO JobProfileResponseDto mapeado.
     */
    @Mapping(source = "name", target = "name")
    JobProfileResponseDto JobProfileToJobProfileResponse (JobProfileEntity jobProfileEntity);
}
