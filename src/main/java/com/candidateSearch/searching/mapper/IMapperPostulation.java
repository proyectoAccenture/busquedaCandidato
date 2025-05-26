package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.PostulationFullResponseDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.entity.PostulationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperPostulation {
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.nameRole", target = "roleName")
    @Mapping(source = "role.description", target = "roleDescription")
    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidate.name", target = "candidateName")
    @Mapping(source = "candidate.lastName", target = "candidateLastName")
    PostulationResponseDto toDto(PostulationEntity postulationEntity);

    @Mapping(source = "candidate.origin.id", target = "candidateResponseDto.originId")
    @Mapping(source = "candidate.origin.name", target = "candidateResponseDto.originName")
    @Mapping(source = "candidate.jobProfile.id", target = "candidateResponseDto.jobProfileId")
    @Mapping(source = "candidate.jobProfile.name", target = "candidateResponseDto.jobProfileName")
    @Mapping(source = "candidate.resumeFileName", target = "candidateResponseDto.resumeFileName")
    @Mapping(target = "candidate.hasResume", expression = "java(candidateEntity.getResumePdf() != null)")

    @Mapping(source = "process.postulation.id", target = "processResponseDto.postulationId")
    @Mapping(source = "process.postulation.candidate.name", target = "processResponseDto.postulationName")

    @Mapping(source = "role.origin.id", target = "roleResponseDto.originId")
    @Mapping(source = "role.origin.name", target = "roleResponseDto.originName")
    @Mapping(source = "role.jobProfile.id", target = "roleResponseDto.jobProfileId")
    @Mapping(source = "role.jobProfile.name", target = "roleResponseDto.jobProfileName")

    @Mapping(source = "candidate", target = "candidateResponseDto")
    @Mapping(source = "process", target = "processResponseDto")
    @Mapping(source = "role", target = "roleResponseDto")
    PostulationFullResponseDto toDtoFull(PostulationEntity postulation);
}
