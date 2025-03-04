package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidateRequest {
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "card", source = "card")
    CandidateEntity CandidateRequestToCandidate (CandidateRequestDto candidateRequestDto);

}
