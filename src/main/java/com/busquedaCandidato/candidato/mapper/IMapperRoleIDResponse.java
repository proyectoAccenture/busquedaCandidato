package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperRoleIDResponse {
    RoleIDResponseDto toDto(RoleIDEntity roleIDEntity);
}
