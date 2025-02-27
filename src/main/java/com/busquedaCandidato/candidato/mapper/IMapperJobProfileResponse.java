package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface IMapperJobProfileResponse {

    @Mapping(source = "name", target = "name")
    JobProfileResponseDto JobProfileToStatusResponse (JobProfileEntity jobProfileEntity);
}
