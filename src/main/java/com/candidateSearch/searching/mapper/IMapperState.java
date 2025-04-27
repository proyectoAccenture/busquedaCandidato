package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.request.StateRequestDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.entity.StateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperState {
     StateResponseDto toDto(StateEntity stateEntity);
     StateEntity toEntity(StateRequestDto stateRequestDto);
}
