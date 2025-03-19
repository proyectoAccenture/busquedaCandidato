package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperJobProfileResponse {
    JobProfileResponseDto toDto(JobProfileEntity jobProfileEntity);
}
