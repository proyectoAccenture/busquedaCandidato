package com.busquedaCandidato.candidato.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CandidateResumeResponse", description = "Información de la hoja de vida de un candidato")
public class CandidateResumeResponseDto {
    @Schema(name = "candidateId", description = "ID del candidato")
    private Long candidateId;

    @Schema(name = "resumeFileName", description = "Nombre del archivo de la hoja de vida")
    private String resumeFileName;

    @Schema(name = "resumeContentType", description = "Tipo de contenido del archivo")
    private String resumeContentType;

    @Schema(name = "resumePdf", description = "Contenido del archivo PDF")
    private byte[] resumePdf;
}
