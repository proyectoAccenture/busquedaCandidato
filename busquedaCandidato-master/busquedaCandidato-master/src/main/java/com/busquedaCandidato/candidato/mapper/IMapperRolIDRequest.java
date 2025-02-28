package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.request.RolIDRequestDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;

/**
 * Interfaz para mapear RolIDRequestDto a RolIDEntity.
 * Utiliza MapStruct para la implementación del mapeo.
 */

@Mapper(componentModel = "spring")
public interface IMapperRolIDRequest {
/**
     * Mapea RoleIDRequestDto a RoleIDEntity.
     *
     * @param rolIDRequestDto El DTO que representa una solicitud de role id.
     * @return La entidad RoleIDEntity mapeada.
     */
    @Mapping(source = "name", target = "name")
    RoleIDEntity RolIDRequestToStatus(RolIDRequestDto rolIDRequestDto);
}
