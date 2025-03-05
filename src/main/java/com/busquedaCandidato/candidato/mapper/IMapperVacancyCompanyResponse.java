package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.VacancyCompanyResponseDto;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperVacancyCompanyResponse {
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "jobProfile.id", target = "jobProfileId")
    @Mapping(source = "jobProfile.name", target = "jobProfileName")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "origin.name", target = "originName")
    VacancyCompanyResponseDto VacancyCompanyToVacancyCompanyResponse (VacancyCompanyEntity vacancyCompanyEntity);
}
