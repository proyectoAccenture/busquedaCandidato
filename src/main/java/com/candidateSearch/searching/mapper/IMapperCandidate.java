package com.candidateSearch.searching.mapper;

import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IMapperCandidate {
    @Mapping(source = "jobProfile.id", target = "jobProfileId")
    @Mapping(source = "jobProfile.name", target = "jobProfileName")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "origin.name", target = "originName")
    @Mapping(target = "hasResume", expression = "java(candidateEntity.getResumePdf() != null)")
    @Mapping(source = "resumeFileName", target = "resumeFileName")
    CandidateResponseDto toDto(CandidateEntity candidateEntity);
}
