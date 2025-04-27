package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.CandidateStateResponseDto;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidateState {
    @Mapping(source = "process.id", target = "processId")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    CandidateStateResponseDto toDto(CandidateStateEntity candidateStateEntity);
}
