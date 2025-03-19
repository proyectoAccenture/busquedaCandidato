package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperJobProfileRequest {
    JobProfileEntity toEntity(JobProfileRequestDto jobProfileRequestDto);
}
