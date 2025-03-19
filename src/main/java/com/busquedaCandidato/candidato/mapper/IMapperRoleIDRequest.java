package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperRoleIDRequest {
    RoleIDEntity toEntity(RoleIDRequestDto roleIDRequestDto);
}
