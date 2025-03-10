package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.PhaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;

@Mapper(componentModel = "spring")
public interface IMapperPhaseResponse {
     @Mapping(source = "name", target = "name")
    PhaseResponseDto PhaseToPhaseResponse (PhaseEntity phaseEntity);

}
