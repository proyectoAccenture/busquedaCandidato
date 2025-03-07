package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(name = "CandidatePhasesResponse",description = "Model representing a candidate phases process in the database")
@Data
public class CandidatePhasesResponseDto {
    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the candidate phases process in the database")
    private Long id;

    //_@Schema(name = "processId")
    //private Long processId;

    @Schema(name = "phaseId")
    private Long phaseId;

    @Schema(name = "phaseName")
    private String phaseName;

    @Schema(name = "stateId")
    private Long stateId;

    @Schema(name = "stateName")
    private String stateName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
}
