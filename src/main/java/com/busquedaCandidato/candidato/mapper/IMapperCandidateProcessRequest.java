package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestDto;
import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidateProcessRequest {
    @Mapping(target = "description", source = "description")
    CandidateProcessEntity CandidateProcessResquestToCandidateProcess (CandidateProcessRequestDto candidateProcessRequestDto);
}
