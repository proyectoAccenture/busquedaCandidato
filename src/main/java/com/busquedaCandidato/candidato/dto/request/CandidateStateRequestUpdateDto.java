package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Pattern(regexp = "^[a-zA-Z0-9 .,!?-]+$", message = "Description contains invalid characters")
    private String description;

    @Schema(name = "status", description = "Status in which a candidate state is located")
    @NotNull(message = "Status cannot be null")
    private Boolean status;

    @Schema(name = "assignedDate", description = "Name of the job profile", example = "2025-01-01")
    @NotNull(message = "Date in which a candidate is doing the state")
    private LocalDate assignedDate;
}
