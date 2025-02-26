package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;


@Mapper(componentModel = "spring")
public interface IMapperJobProfileResponse {

    @Mapping(source = "name", target = "name")
    JobProfileResponseDto JobProfileToJobProfileResponse (JobProfileEntity jobProfileEntityEntity);
}
