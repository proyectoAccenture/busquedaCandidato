package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RoleResponseDto;
import com.busquedaCandidato.candidato.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRoleResponse {
    @Mapping(source = "companyVacancy.id", target = "companyVacancyId")
    @Mapping(source = "companyVacancy.jobProfile.name", target = "companyVacancyName")
    RoleResponseDto toDto(RoleEntity roleEntity);
}
