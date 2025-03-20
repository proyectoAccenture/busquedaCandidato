package com.busquedaCandidato.candidato.dto.response;

import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "ProcessResponse", description = "Model represent a candidate process on database")
@Data
public class ProcessResponseDto {

    @Schema(name = "id", description=  "Unique Id of the candidate status history  in the database")
    private Long id;

    @Schema(name = "postulationId", description = "Id of postulation in this process")
    private Long postulationId;

    @Schema(name = "postulationName", description = "Name of the candidate in this process ")
    private String postulationName;

    @Schema(name = "description", description = "Description in this process")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;

    @Schema(name = "ListCandidateProcess",description = "List all phases that is the candidate")
    private List<CandidatePhasesEntity> candidatePhases;
}
