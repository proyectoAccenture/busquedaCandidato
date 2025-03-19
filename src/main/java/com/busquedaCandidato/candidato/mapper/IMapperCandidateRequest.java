package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperCandidateRequest {
    CandidateEntity toEntity(CandidateRequestDto candidateRequestDto);
}
