package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "OriginRequest", description = "Model represent a origin on database")
@Data
public class OriginRequestDto {

    @Schema(name = "name", description = "Name of origin", example = "Validation")
    @NotNull(message = "Origin cannot be null")
    @Size(min = 1, max = 25, message = "The origin name must be greater than 0 and less than 10")
    private final String name;

}
