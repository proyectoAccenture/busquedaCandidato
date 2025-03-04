package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateProcessResponseDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperProcessResponse {
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidate.name", target = "candidateName")
    ProcessResponseDto ProcessToProcessResponse (ProcessEntity processEntity);

}
