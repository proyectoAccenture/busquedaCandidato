package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.CandidatePhasesResponseDto;
import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidatePhasesResponse {
    @Mapping(source = "process.id", target = "processId")
    @Mapping(source = "phase.id", target = "phaseId")
    @Mapping(source = "phase.name", target = "phaseName")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    @Mapping(source = "process.postulation.vacancyCompany.id", target = "vacancyId")
    @Mapping(source = "process.postulation.vacancyCompany.role.name", target = "roleName")
    @Mapping(source = "process.postulation.vacancyCompany.description", target = "companyName")
    CandidatePhasesResponseDto toDto(CandidatePhasesEntity candidatePhasesEntity);
}
