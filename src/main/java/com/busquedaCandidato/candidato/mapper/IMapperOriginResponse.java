package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperOriginResponse {
    OriginResponseDto toDto(OriginEntity originEntity);
}
