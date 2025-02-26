package com.busquedaCandidato.candidato.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "OriginResponse", description = "Model represent a origin on database")
@Data
public class OriginResponseDto {
    @Schema(name = "originId", defaultValue = "1", description = "Unique Id of origin on database")
    private final Long id;

    @Schema(name = "name", description = "Origin name")
    private final String name;

}
