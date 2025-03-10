package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;

@Mapper(componentModel = "spring")
public interface IMapperStateResponse {
     @Mapping(source = "name", target = "name")
    StateResponseDto StateToStateResponse(StateEntity stateEntity);


}
