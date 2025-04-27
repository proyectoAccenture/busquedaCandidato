package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.ProcessResponseDto;
import com.candidateSearch.searching.entity.ProcessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperProcess {
    @Mapping(source = "postulation.id", target = "postulationId")
    @Mapping(source = "postulation.candidate.name", target = "postulationName")
    @Mapping(source = "candidateState", target = "candidateState")
    ProcessResponseDto toDto(ProcessEntity processEntity);
}
