package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRoleIDResponse {
    @Mapping(source = "vacancyCompanyEntity.id", target = "vacancyCompanyId")
    @Mapping(source = "vacancyCompanyEntity.jobProfile.name", target = "vacancyCompanyName")
    RoleIDResponseDto toDto(RoleIDEntity roleIDEntity);
}
