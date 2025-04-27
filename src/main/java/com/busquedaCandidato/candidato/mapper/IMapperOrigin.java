package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperOrigin {
    OriginEntity toEntity(OriginRequestDto originRequestDto);
    OriginResponseDto toDto(OriginEntity originEntity);
}
