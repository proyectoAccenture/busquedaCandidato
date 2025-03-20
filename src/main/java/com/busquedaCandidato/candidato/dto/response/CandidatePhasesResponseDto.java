package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CandidatePhasesResponse",description = "Model representing a candidate phases process in the database")
public class CandidatePhasesResponseDto {
    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the candidate phases process in the database")
    private Long id;

    @Schema(name = "processId", description = "Id of process of the candidate")
    private Long processId;

    @Schema(name = "phaseId", description = "Id of phase where is of the candidate")
    private Long phaseId;

    @Schema(name = "phaseName", description = "Name of phase where is of the candidate")
    private String phaseName;

    @Schema(name = "stateId", description = "Id of state where is of the candidate")
    private Long stateId;

    @Schema(name = "stateName", description = "Name of state where is of the candidate")
    private String stateName;

    @Schema(name = "status", description = "Status of the candidate")
    private Boolean status;

    @Schema(name = "description", description = "Description of the candidate")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;

    @Schema(name = "vacancyId", description = "Id of the vacancy")
    private Long vacancyId;

    @Schema(name = "roleName", description = "Role name of the vacancy")
    private String roleName;

    @Schema(name = "companyName", description = "Company associated with the vacancy")
    private String companyName;
}
