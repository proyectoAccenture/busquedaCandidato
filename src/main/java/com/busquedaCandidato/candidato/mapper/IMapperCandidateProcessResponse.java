package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.CandidateProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidateProcessResponse {
    @Mapping(source = "process.id", target = "processId")
    @Mapping(source = "phase.id", target = "phaseId")
    @Mapping(source = "phase.name", target = "phaseName")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    CandidateProcessResponseDto CandidateProcessToCandidateProcessResponse (CandidateProcessEntity candidateProcessEntity);
}

