package com.busquedaCandidato.candidato.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "JobProfileResponse", description = "Model represent a jobprofile on database")
@Data
public class JobProfileResponseDto {
    @Schema(name = "jobprofileId", defaultValue = "1", description = "Unique Id of jobprofile on database")
    private final Long id;

    @Schema(name = "name", description = "JobProfile name")
    private final String name;
}
