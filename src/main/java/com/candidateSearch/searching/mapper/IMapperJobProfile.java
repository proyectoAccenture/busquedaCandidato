package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.request.JobProfileRequestDto;
import com.candidateSearch.searching.dto.response.JobProfileResponseDto;
import com.candidateSearch.searching.entity.JobProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperJobProfile {
    JobProfileEntity toEntity(JobProfileRequestDto jobProfileRequestDto);
    JobProfileResponseDto toDto(JobProfileEntity jobProfileEntity);
}
