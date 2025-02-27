package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import com.busquedaCandidato.candidato.entity.OriginEntity;


@Mapper(componentModel = "spring")
public interface IMapperOriginRequest {
    @Mapping(source = "name", target = "name")
    OriginEntity OriginRequestToOrigin (OriginRequestDto originRequestDto);

}
