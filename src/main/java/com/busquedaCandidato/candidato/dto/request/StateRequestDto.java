package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "StateRequest", description = "Model represent a state on database")
@Data
@NoArgsConstructor
public class StateRequestDto {

    @Schema(name = "name", description = "Name of state", example = "string")
    @NotNull(message = "State cannot be null")
    @Size(min = 1, max = 250, message = "The state name must be greater than 0 and less than 250")
    private String name;
}
