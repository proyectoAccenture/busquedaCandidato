package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateProcessRequestUpdate", description = "The same candidate process model but only for updating")
public class CandidateProcessRequestUpdateDto {
    @Schema(name = "stateId",description = "Id of state", example = "1")
    @NotNull(message = "state cannot be null")
    private Long stateId;

    @Schema(name = "description", description = "Description about candidate process", example = "This process is...")
    private String description;

    @Schema(name = "status",description = "Status in which a candidate is located")
    private Boolean status;

    @Schema(name = "assignedDate",description = "Name of the job profile", example = "2025-01-30")
    @NotNull(message = "Date in which a candidate is doing the phase")
    private LocalDate assignedDate;
}
