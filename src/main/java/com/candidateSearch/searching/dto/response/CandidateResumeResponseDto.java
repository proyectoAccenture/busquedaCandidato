package com.candidateSearch.searching.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CandidateResumeResponse", description = "Information from a candidate's resume")
public class CandidateResumeResponseDto {

    @Schema(name = "candidateId", description = "candidate ID")
    private Long candidateId;

    @Schema(name = "resumeFileName", description = "Resume file name")
    private String resumeFileName;

    @Schema(name = "resumeContentType", description = "File content type")
    private String resumeContentType;

    @Schema(name = "resumePdf", description = "PDF file content")
    private byte[] resumePdf;
}
