package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperOriginRequest {
    OriginEntity toEntity(OriginRequestDto originRequestDto);
}
