package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperProcessResponse {
    @Mapping(source = "postulation.id", target = "postulationId")
    @Mapping(source = "postulation.candidate.name", target = "postulationName")
    ProcessResponseDto ProcessToProcessResponse (ProcessEntity processEntity);
}
