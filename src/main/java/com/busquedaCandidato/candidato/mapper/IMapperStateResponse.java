package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;

@Mapper(componentModel = "spring")
public interface IMapperStateResponse {
     @Mapping(source = "name", target = "name")
    StateResponseDto StateToStateResponse(StateEntity stateEntity);


}
