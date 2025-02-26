package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.RolIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;

/**
 * Interfaz para mapear RolIDEntity a RolIDResponseDto.
 * Utiliza MapStruct para la implementación del mapeo.
 */
@Mapper(componentModel = "spring")
public interface IMapperRolIDResponse {
  
    /**
     * Mapea RoleIDEntity a RoleIDResponseDto.
     *
     * @param jobProfileEntity La entidad que representa un Role ID.
     * @return El DTO RolIDResponseDto mapeado.
     */

    @Mapping(source = "name", target = "name")
    RolIDResponseDto RolIDToStatusResponse (RoleIDEntity roleIDEntity);
    }
