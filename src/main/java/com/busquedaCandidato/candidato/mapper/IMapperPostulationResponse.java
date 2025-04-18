package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperPostulationResponse {
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidate.name", target = "candidateName")
    @Mapping(source = "candidate.lastName", target = "candidateLastName")
    PostulationResponseDto toDto(PostulationEntity postulationEntity);
}
