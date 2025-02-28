package com.busquedaCandidato.candidato.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "RolIDResponse", description = "Model representing a Role ID in the database")
@Data
public class RolIDResponseDto {
    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the Role ID in the database")
    private final  Long id;

    @Schema(name = "name",description = "Role ID name")
    private final String name;
}
