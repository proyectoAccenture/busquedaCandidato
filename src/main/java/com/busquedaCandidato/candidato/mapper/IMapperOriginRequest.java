package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.OriginEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;


@Mapper(componentModel = "spring")
public interface IMapperOriginRequest {
    @Mapping(source = "name", target = "name")
    OriginEntity OriginRequestToOrigin (OriginRequestDto originRequestDto);

}
