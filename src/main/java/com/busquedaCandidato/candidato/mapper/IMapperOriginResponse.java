package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.OriginEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;

@Mapper(componentModel = "spring")
public interface IMapperOriginResponse {

    @Mapping(source = "name", target = "name")
    OriginResponseDto OriginToOriginResponse (OriginEntity originEntity);


}
