package com.busquedaCandidato.candidato.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "JobProfileRequest", description = "Model representing a job profile in the database")
@Data
@NoArgsConstructor
public class JobProfileRequestDto {

    @Schema(name = "name",description = "Name of the job profile", example = "string")
    @NotNull(message = "jobrofile cannot be null")
    @Size(min = 1, max = 50, message = "The job profile name must be greater than 0 and less than 100")
    private String name;
}
