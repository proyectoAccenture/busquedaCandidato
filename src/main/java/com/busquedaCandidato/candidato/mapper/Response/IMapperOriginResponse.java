package com.busquedaCandidato.candidato.mapper.Response;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.entity.OriginEntity;

@Mapper(componentModel = "spring")
public interface IMapperOriginResponse {

    @Mapping(source = "name", target = "name")
    OriginResponseDto OriginToOriginResponse (OriginEntity originEntity);


}
