package com.busquedaCandidato.candidato.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "PhaseResponse", description = "Model represent a phase on database")
@Data
public class PhaseResponseDto {
    @Schema(name = "id", defaultValue = "1", description = "Unique Id of phase on database")
    private Long id;
    @Schema(name = "name", description = "Phase name")
    private String name;
}
