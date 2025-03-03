package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "OriginRequest", description = "Model represent a origin on database")
@Data
public class OriginRequestDto {
    @NotBlank(message = "The origin name cannot be null or blank")
    @Schema(name = "name", description = "Name of origin", example = "Validation")
    @Size(min = 1, max = 25, message = "The origin name must be greater than 0 and less than 25")
    private final String name;

}
