package com.candidateSearch.searching.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "PostulationFullResponse",description = "Model representing a postulation full of the candidate on database")
@Data
public class PostulationFullResponseDto {

    @Schema(name = "candidateResponseDto",description = "Candidate", example = "candidate")
    private CandidateResponseDto candidateResponseDto;

    @Schema(name = "processResponseDto",description = "Process", example = "process")
    private ProcessResponseDto processResponseDto;

    @Schema(name = "roleResponseDto",description = "Role", example = "role")
    private RoleResponseDto roleResponseDto;
}
