package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(name = "PostulationResponse",description = "Model representing a postulation of the candidate on database")
@Data
public class PostulationResponseDto {

    @Schema(name = "id", defaultValue = "1", description = "Unique Id of postulation on database")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePresentation;

    @Schema(name = "salaryAspiration", description = "the salary you hope to have ", example = "0.000.000")
    private String salaryAspiration;

    @Schema(name = "salaryCurrent", description = "the salary you have in this moment", example = "0.000.000")
    private String salaryCurrent;

    @Schema(name = "experience", description = "write the experience you have", example = "5 year")
    private String experience;

    @Schema(name = "level", description = "level you have", example = "Senior")
    private String level;

    @Schema(name = "skills", description = "skills you have", example = "Java, AWS, Angular")
    private String skills;

    @Schema(name = "vacancyCompanyId",description = "Id of tha vacancy",example = "1")
    private Long vacancyCompanyId;

    @Schema(name = "vacancyCompanyName",description = "Name of tha vacancy",example = "1")
    private String vacancyCompanyName;

    @Schema(name = "candidateId",description = "Id of candidate that appliqued", example = "1")
    private Long candidateId;

    @Schema(name = "candidateName",description = "Name of candidate that appliqued", example = "1")
    private String candidateName;
}
