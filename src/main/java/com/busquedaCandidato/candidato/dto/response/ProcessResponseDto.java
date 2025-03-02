package com.busquedaCandidato.candidato.dto.response;

import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(name = "ProcessResponse", description = "Model represent a state on database")
@Data
public class ProcessResponseDto {

    @Schema(name = "id", description=  "Unique Id of the process in the database")
    private Long id;

    @Schema(name = "candidateId", description = "Candidate in this process")
    private Long candidateId;

    @Schema(name = "candidateName", description = "Name of candidate that is on this process")
    private String candidateName;

    @Schema(name = "ListCandidateProcess",description = "List all phases that is the candidate")
    private List<CandidateProcessEntity> processPhases;
}
