package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz para mapear RoleIDEntity a RoleIDResponseDto.
 * Utiliza MapStruct para la implementación del mapeo.
 */
@Mapper(componentModel = "spring")
public interface IMapperRoleIDResponse {
    /**
     * Mapea RoleIDEntity a RoleIDResponseDto.
     *
     * @param roleIDEntity La entidad que representa un Role ID.
     * @return El DTO RolIDResponseDto mapeado.
     */

    @Mapping(source = "name", target = "name")
    RoleIDResponseDto RolIDToStatusResponse (RoleIDEntity roleIDEntity);
}
