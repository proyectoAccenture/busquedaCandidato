package com.candidateSearch.searching.dto.response;

import com.candidateSearch.searching.utility.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CandidateStateResponse",description = "Model representing a candidate state process in the database")
public class CandidateStateResponseDto {

    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the candidate state process in the database")
    private Long id;

    @Schema(name = "processId", description = "Id of process of the candidate")
    private Long processId;

    @Schema(name = "stateId", description = "Id of state where is of the candidate")
    private Long stateId;

    @Schema(name = "stateName", description = "Name of state where is of the candidate")
    private String stateName;

    @Schema(name = "statusHistory", description = "Status of the candidate state (true if active, false if closed)", example = "false")
    private Status statusHistory;

    @Schema(name = "status", description = "Status of the candidate state (true if active, false if closed)", example = "false")
    private Boolean status;

    @Schema(name = "description", description = "Description of the candidate")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
}
