package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.entity.PostulationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperPostulation {
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.jobProfile.name", target = "roleName")
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidate.name", target = "candidateName")
    @Mapping(source = "candidate.lastName", target = "candidateLastName")
    PostulationResponseDto toDto(PostulationEntity postulationEntity);
}
