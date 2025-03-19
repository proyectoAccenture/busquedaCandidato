package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperPostulationResponse {
    @Mapping(source = "vacancyCompany.id", target = "vacancyCompanyId")
    @Mapping(source = "vacancyCompany.jobProfile.name", target = "vacancyCompanyName")
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidate.name", target = "candidateName")
    PostulationResponseDto toDto(PostulationEntity postulationEntity);
}
