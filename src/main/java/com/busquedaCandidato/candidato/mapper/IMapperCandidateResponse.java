package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidateResponse {
    @Mapping(source = "jobProfile.id", target = "jobProfileId")
    @Mapping(source = "jobProfile.name", target = "jobProfileName")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "origin.name", target = "originName")
    CandidateResponseDto toDto(CandidateEntity candidateEntity);
}
