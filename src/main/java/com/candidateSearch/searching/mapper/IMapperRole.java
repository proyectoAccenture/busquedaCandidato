package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperRole {
    @Mapping(source = "jobProfile.id", target = "jobProfileId")
    @Mapping(source = "jobProfile.name", target = "jobProfileName")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "origin.name", target = "originName")
    RoleResponseDto toDto(RoleEntity roleEntity);
}
