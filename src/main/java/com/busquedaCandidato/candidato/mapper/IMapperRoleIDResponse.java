package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRoleIDResponse {

    @Mapping(source = "name", target = "name")
    RoleIDResponseDto RolIdToRolIdResponse (RoleIDEntity roleIDEntity);
}
