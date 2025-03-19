package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperStateRequest {
     StateEntity toEntity(StateRequestDto stateRequestDto);
}
