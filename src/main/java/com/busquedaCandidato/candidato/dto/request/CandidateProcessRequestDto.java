package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateProcessRequest", description = "Model represent a candidate process on database")
public class CandidateProcessRequestDto {

    @Schema(name = "processId",description = "Id of process of candidate",example = "1")
    @NotNull(message = "processId cannot be null")
    private Long processId;

    @Schema(name = "phaseId",description = "Id of phaseId",example = "1")
    @NotNull(message = "phaseId cannot be null")
    private Long phaseId;

    @Schema(name = "stateId",description = "Id of state", example = "1")
    @NotNull(message = "state cannot be null")
    private Long stateId;

    @Schema(name = "status",description = "Status in which a candidate is located")
    @NotNull(message = "status cannot be null")
    private Boolean status;

    @Schema(name = "description", description = "Description about candidate process", example = "This process is...")
    @NotNull(message = "description cannot be null")
    private String description;

    @Schema(name = "assignedDate", description = "Date when the candidate is assigned", example = "2024-03-01")
    @NotNull(message = "Assigned date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
}
