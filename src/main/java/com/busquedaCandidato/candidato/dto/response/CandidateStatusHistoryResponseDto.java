package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDate;

@Schema(name = "CandidateStatusHistoryResponse", description = "Model represent a candidate status history on database")
@Data
public class CandidateStatusHistoryResponseDto {

    @Schema(name = "id", description=  "Unique Id of the candidate status history  in the database")
    private Long id;

    @Schema(name = "postulationId", description = "postulation in this process")
    private Long postulationId;

    @Schema(name = "candidatePhasesId", description = "candidate Phases in this process")
    private Long candidatePhasesId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;




}
