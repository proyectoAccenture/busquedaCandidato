package com.candidateSearch.searching.dto.response;

import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.utility.Status;
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

    @Schema(name = "status", description = "Status of the process (true if active, false if closed)", example = "false")
    private Status status;

    @Schema(name = "ListCandidateProcess",description = "List all state that is the candidate")
    private List<CandidateStateEntity> candidateState;
}