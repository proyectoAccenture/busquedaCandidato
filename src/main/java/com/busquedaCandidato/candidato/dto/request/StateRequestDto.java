package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "StateRequest", description = "Model represent a state on database")
@Data
@NoArgsConstructor
public class StateRequestDto {

    @Schema(name = "name", description = "Name of state", example = "string")
    @NotBlank(message = "State cannot be blank")
    @Size(min = 1, max = 250, message = "The state name must be greater than 0 and less than 250")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "The State name must only contain letters and spaces")
    private String name;
}
