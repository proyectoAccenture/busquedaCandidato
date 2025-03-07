package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.CandidateStatusHistoryResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateStatusHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidateStatusHistoryResponse {
    @Mapping(source = "postulation.id", target = "postulationId")
    @Mapping(source = "candidatePhases.id", target = "candidatePhasesId")
    CandidateStatusHistoryResponseDto CandidateStatusHistoryToResponse (CandidateStatusHistoryEntity candidateStatusHistoryEntity);

}
