package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "JobProfileRequest", description = "Model represent a jobprofile on database")
@Data
public class JobProfileRequestDto {

    @Schema(name = "name", description = "Name of jobprofile", example = "Validation")
    @NotNull(message = "JobProfile cannot be null")
    @Size(min = 1, max = 25, message = "The jobprofile name must be greater than 0 and less than 10")
    private final String name;
}