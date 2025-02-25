package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperStatusRequest {
    @Mapping(source = "name", target = "name")
    StateEntity StatusRequestToStatus (StateRequestDto stateRequestDto);
}

