package com.busquedaCandidato.candidato.mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.busquedaCandidato.candidato.dto.response.AddStateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IStatusResponse {
    @Mapping(source = "id", target = "id")
    AddStateResponseDto toStateResponse(StateEntity stateEntity);
}
