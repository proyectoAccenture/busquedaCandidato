package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.CompanyVacancyResponseDto;
import com.candidateSearch.searching.entity.CompanyVacancyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperVacancyCompany {
    @Mapping(source = "jobProfile.id", target = "jobProfileId")
    @Mapping(source = "jobProfile.name", target = "jobProfileName")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "origin.name", target = "originName")
    CompanyVacancyResponseDto toDto(CompanyVacancyEntity companyVacancyEntity);
}
