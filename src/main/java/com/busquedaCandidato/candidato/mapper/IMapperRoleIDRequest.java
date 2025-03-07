package com.busquedaCandidato.candidato.mapper;


import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRoleIDRequest {

    @Mapping(source = "name", target = "name")
    RoleIDEntity RolIDRequestToStatus(RoleIDRequestDto roleIDRequestDto);
}
