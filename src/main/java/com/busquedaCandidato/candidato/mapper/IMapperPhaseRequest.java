package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import com.busquedaCandidato.candidato.dto.request.PhaseRequestDto;
import com.busquedaCandidato.candidato.entity.PhaseEntity;

@Mapper(componentModel = "spring")
public interface IMapperPhaseRequest {

    @Mapping(source = "name", target = "name")
    PhaseEntity PhaseRequestToPhase (PhaseRequestDto phaseRequestDto);
}
