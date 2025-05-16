package com.candidateSearch.searching.dto.request;

import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.dto.request.validation.date.DateWithinRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateStateRequestUpdate", description = "The same candidate states model but only for updating")
public class CandidateStateRequestUpdateDto {

    @Schema(name = "stateId",description = "Id of state", example = "1")
    @NotNull(message = "state cannot be null")
    @Min(value = 1, message = "stateId must be greater than 0")
    private Long stateId;

    @Schema(name = "description", description = "Description about candidate state", example = "This process is...")
    @Size(min = 1, max = 255, message = "Description must be between 10 and 255 characters")
    @NotBlank(message = "Description cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9 .,!?-]+$", message = "Description contains invalid characters")
    private String description;

    @Schema(name = "status", description = "Status in which a candidate state is located")
    @NotNull(message = "Status cannot be null")
    private Boolean status;

    @Schema(name = "statusHistory", description = "Status of process = 'ACTIVE', 'INACTIVE' ", example = "ACTIVE")
    @NotNull(message = "Status cannot be null")
    private Status statusHistory;

    @Schema(name = "assignedDate", description = "Name of the job profile", example = "2025-01-01")
    @NotNull(message = "Date in which a candidate is doing the state")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateWithinRange
    private LocalDate assignedDate;
}
