package com.busquedaCandidato.candidato.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "JobProfileResponse",description = "Model representing a job profile in the database")
@Data
public class JobProfileResponseDto {
    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the job profile in the database")
    private Long id;

    @Schema(name = "name",description = "Job profile name")
    private String name;
}
