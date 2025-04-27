package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRole {
    @Mapping(source = "companyVacancy.id", target = "companyVacancyId")
    @Mapping(source = "companyVacancy.jobProfile.name", target = "companyVacancyName")
    RoleResponseDto toDto(RoleEntity roleEntity);
}
