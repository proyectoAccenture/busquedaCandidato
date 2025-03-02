package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperProcessRequest {
    ProcessEntity ProcessResquestToProcessEntity (ProcessRequestDto processRequestDto);
}
