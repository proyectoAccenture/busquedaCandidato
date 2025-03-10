package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;

@Mapper(componentModel = "spring")
public interface IMapperStateRequest {

     @Mapping(source = "name", target = "name")
     StateEntity StateResquestToState (StateRequestDto stateRequestDto);
}
