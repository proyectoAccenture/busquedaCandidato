package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProcessRequestDto {
    @Schema(name = "CandidateId",description = "Id of candidate",example = "1")
    @NotNull(message = "candidateId cannot be null")
    private Long candidateId;
}
