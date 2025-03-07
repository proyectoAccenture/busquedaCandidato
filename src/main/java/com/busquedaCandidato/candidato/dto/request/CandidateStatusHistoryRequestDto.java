package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "CandidateStatusHistoryRequest", description = "Model represent a candidate Phases history on database")
public class CandidateStatusHistoryRequestDto {
    @Schema(name = "postulationId",description = "Id of postulation", example = "1")
    @NotNull(message = "postulationId cannot be null")
    private Long postulationId;

    @Schema(name = "candidatePhasesId",description = "Id of candidate Phases", example = "1")
    @NotNull(message = "candidatePhasesId cannot be null")
    private Long candidatePhasesId;


    @Schema(name = "description", description = "Description about candidate Phases", example = "This process is...")
    @NotNull(message = "description cannot be null")
    private String description;


}
