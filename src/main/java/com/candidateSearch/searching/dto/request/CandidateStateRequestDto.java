package com.candidateSearch.searching.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateStateRequest", description = "Model represent a candidate state process on database")
public class CandidateStateRequestDto {

    @Schema(name = "processId",description = "Id of process of candidate",example = "1")
    @NotNull(message = "processId cannot be null")
    @Min(value = 1, message = "processId must be greater than 0")
    private Long processId;

    @Schema(name = "stateId", description = "Id of stateId", example = "1")
    @NotNull(message = "State cannot be null")
    @Min(value = 1, message = "stateId must be greater than 0")
    private Long stateId;

    @Schema(name = "status",description = "Status in which a candidate is located")
    @NotNull(message = "Status cannot be null")
    private Boolean status;

    @Schema(name = "description", description = "Description about candida te process", example = "This process is...")
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 10 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 .,!?-]+$", message = "Description contains invalid characters")
    private String description;

    @Schema(name = "assignedDate", description = "Date when the candidate is assigned", example = "2024-03-01")
    @NotNull(message = "Assigned date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
}
