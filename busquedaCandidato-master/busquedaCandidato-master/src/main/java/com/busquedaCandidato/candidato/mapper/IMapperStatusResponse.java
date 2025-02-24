package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperStatusResponse {

    @Mapping(source = "name", target = "state")
    StateResponseDto StatusToStatusResponse (StateEntity stateEntity);

    @Mapping(source = "state", target = "name")
    StateEntity StatusResponseToStatus (StateEntity stateResponseDto);
}
