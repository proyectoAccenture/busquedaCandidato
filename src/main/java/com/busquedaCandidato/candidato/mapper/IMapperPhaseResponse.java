package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;
import com.busquedaCandidato.candidato.entity.PhaseEntity;

@Mapper(componentModel = "spring")
public interface IMapperPhaseResponse {
     @Mapping(source = "name", target = "name")
    PhaseResponseDto PhaseToPhaseResponse (PhaseEntity phaseEntity);

}
