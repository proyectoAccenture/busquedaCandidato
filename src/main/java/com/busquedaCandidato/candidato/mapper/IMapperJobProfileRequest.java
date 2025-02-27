package com.busquedaCandidato.candidato.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;


@Mapper(componentModel = "spring")
public interface IMapperJobProfileRequest {
     @Mapping(source = "name", target = "name")
    JobProfileEntity JobProfileResquestToStatus (JobProfileRequestDto jobProfileRequestDto);


}
