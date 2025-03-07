package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperJobProfileRequest {

    @Mapping(source = "name", target = "name")
    JobProfileEntity  JobProfileResquestToJobProfile(JobProfileRequestDto jobProfileRequestDto);


}
