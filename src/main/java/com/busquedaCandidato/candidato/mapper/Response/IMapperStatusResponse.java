package com.busquedaCandidato.candidato.mapper.Response;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperStatusResponse {

    @Mapping(source = "name", target = "name")
    StateResponseDto StatusToStatusResponse (StateEntity stateEntity);

   


}
