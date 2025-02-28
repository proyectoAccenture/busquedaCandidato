package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "RolIDRequest", description = "Model representing a RolID in the database")
@Data
public class RolIDRequestDto {
    @Schema(name = "name",description = "Name of the Role ID",example = "Role of the vacancy")
    @NotNull(message = "Role ID cannot be null")
    @Size(min = 1, max = 100, message = "The Role ID name must be greater than 0 and less than 100")
    private  final  String name;
}
