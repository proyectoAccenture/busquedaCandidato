package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.PhaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import com.busquedaCandidato.candidato.dto.request.PhaseRequestDto;

@Mapper(componentModel = "spring")
public interface IMapperPhaseRequest {

    @Mapping(source = "name", target = "name")
    PhaseEntity PhaseRequestToPhase (PhaseRequestDto phaseRequestDto);
}
