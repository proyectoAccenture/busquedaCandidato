package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.CandidatePhasesResponseDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { IMapperCandidatePhasesResponse.class })
public interface IMapperCandidateResponse {

    @Mapping(source = "postulations", target = "phases", qualifiedByName = "mapPostulationsToPhases")
    CandidateResponseDto toDto(CandidateEntity candidateEntity);

    @Named("mapPostulationsToPhases")
    default List<CandidatePhasesResponseDto> mapPostulationsToPhases(List<PostulationEntity> postulations) {
        if (postulations == null) {
            return Collections.emptyList();
        }
        return postulations.stream()
                .map(PostulationEntity::getProcess)
                .filter(Objects::nonNull)
                .flatMap(process -> process.getCandidatePhases().stream())
                .filter(Objects::nonNull)
                .map(IMapperCandidatePhasesResponse.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
}
