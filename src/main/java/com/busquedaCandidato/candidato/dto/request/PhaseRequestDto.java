package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "PhaseRequest", description = "Model represent a phase on database")
@Data
public class PhaseRequestDto {
    @Schema(name = "name", description = "Name of phase", example = "Validation")
    @NotNull(message = "Phase cannot be null")
    @Size(min = 1, max = 25, message = "The phase name must be greater than 0 and less than 25")
    private final String name;
}


