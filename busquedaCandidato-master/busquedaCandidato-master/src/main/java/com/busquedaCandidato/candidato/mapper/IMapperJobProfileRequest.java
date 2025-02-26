package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;


public interface IMapperJobProfileRequest {
@Mapping(source = "name", target = "name")
    JobProfileEntity JobProfileRequestToJobProfile (JobProfileRequestDto jobprofileRequestDto);
}
