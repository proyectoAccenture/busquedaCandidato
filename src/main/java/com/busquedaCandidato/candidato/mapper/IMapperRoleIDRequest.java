package com.busquedaCandidato.candidato.mapper;


import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz para mapear RolIDRequestDto a RolIDEntity.
 * Utiliza MapStruct para la implementación del mapeo.
 */

@Mapper(componentModel = "spring")
public interface IMapperRoleIDRequest {
    /**
     * Mapea RoleIDRequestDto a RoleIDEntity.
     *
     * @param roleIDRequestDto El DTO que representa una solicitud de role id.
     * @return La entidad RoleIDEntity mapeada.
     */
    @Mapping(source = "name", target = "name")
    RoleIDEntity RolIDRequestToStatus(RoleIDRequestDto roleIDRequestDto);
}
