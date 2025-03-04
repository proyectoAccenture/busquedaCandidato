package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "ProcessRequest", description = "Model represent a process on database")
public class ProcessRequestDto {
    @Schema(name = "candidateId",description = "Id of candidate", example = "1")
    @NotNull(message = "candidateId cannot be null")
    private Long candidateId;
}
