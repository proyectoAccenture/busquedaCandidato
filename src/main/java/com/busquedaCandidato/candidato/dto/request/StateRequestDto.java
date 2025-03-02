package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "StateRequest", description = "Model represent a state on database")
@Data
public class StateRequestDto {

    @Schema(name = "name", description = "Name of state")
    @NotNull(message = "State cannot be null")
    @Size(min = 1, max = 250, message = "The state name must be greater than 0 and less than 250")
    private final String name;
}
