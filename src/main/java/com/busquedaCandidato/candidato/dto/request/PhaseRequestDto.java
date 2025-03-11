package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "PhaseRequest", description = "Model represent a phase on database")
@Data
@NoArgsConstructor
public class PhaseRequestDto {
    @Schema(name = "name", description = "Name of the phase", example = "Interview")
    @NotBlank(message = "Phase cannot be blank")
    @Size(min = 1, max = 250, message = "The phase name must be between 1 and 250 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The phase name must not contain special characters or numbers")
    private String name;
}


