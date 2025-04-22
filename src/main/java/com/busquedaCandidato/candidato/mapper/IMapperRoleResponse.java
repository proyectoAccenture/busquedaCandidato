package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RoleResponseDto;
import com.busquedaCandidato.candidato.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRoleResponse {
    @Mapping(source = "companyVacancyEntity.id", target = "companyVacancyId")
    @Mapping(source = "companyVacancyEntity.jobProfile.name", target = "companyVacancyName")
    RoleResponseDto toDto(RoleEntity roleEntity);
}
