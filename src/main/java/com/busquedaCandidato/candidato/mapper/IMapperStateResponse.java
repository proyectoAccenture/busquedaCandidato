package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperStateResponse {
    StateResponseDto toDto(StateEntity stateEntity);
}
