package com.candidateSearch.searching.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NextValidStatesResponse", description = "Valid status information with candidate and process data")
public class NextValidStatesResponseDto {

    @Schema(name = "candidateId", description = "candidate ID")
    private Long candidateId;

    @Schema(name = "candidateName", description = "Candidate name")
    private String candidateName;

    @Schema(name = "candidateLastName", description = "Candidate's last name")
    private String candidateLastName;

    @Schema(name = "processId", description = "process ID")
    private Long processId;

    @Schema(name = "processDescription", description = "Process description")
    private String processDescription;

    @Schema(name = "nextValidStates", description = "List of valid states below")
    private List<StateResponseDto> nextValidStates;
}
