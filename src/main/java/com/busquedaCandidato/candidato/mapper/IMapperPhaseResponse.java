package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperPhaseResponse {
    PhaseResponseDto toDto(PhaseEntity phaseEntity);
}
