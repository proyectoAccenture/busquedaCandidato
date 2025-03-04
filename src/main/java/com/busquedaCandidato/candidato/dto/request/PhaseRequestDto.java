package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "PhaseRequest", description = "Model represent a phase on database")
@Data
@NoArgsConstructor
public class PhaseRequestDto {
    @Schema(name = "name", description = "Name of phase")
    @NotNull(message = "Phase cannot be null")
    @Size(min = 1, max = 250, message = "The phase name must be greater than 0 and less than 250")
    private String name;
}


