package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.OriginResponseDto;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.dto.request.OriginRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperOrigin {
    OriginEntity toEntity(OriginRequestDto originRequestDto);
    OriginResponseDto toDto(OriginEntity originEntity);
}
