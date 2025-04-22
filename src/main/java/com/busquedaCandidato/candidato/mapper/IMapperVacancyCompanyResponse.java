package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.CompanyVacancyResponseDto;
import com.busquedaCandidato.candidato.entity.CompanyVacancyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperVacancyCompanyResponse {
    @Mapping(source = "jobProfile.id", target = "jobProfileId")
    @Mapping(source = "jobProfile.name", target = "jobProfileName")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "origin.name", target = "originName")
    CompanyVacancyResponseDto toDto(CompanyVacancyEntity companyVacancyEntity);
}
