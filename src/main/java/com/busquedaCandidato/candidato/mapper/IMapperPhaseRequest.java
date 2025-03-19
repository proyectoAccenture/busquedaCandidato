package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.dto.request.PhaseRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperPhaseRequest {
    PhaseEntity toEntity(PhaseRequestDto phaseRequestDto);
}
