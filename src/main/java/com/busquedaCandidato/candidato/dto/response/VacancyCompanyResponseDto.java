package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(name = "VacancyCompanyResponse", description = "Model represent a vacancy on database")
public class VacancyCompanyResponseDto {
    @Schema(name = "id", defaultValue = "1", description = "Unique Id of origin on database")
    private final Long id;

    @Schema(name = "contract", description = "contract of the candidate", example = "string")
    private String contract;

    @Schema(name = "salary", description = "salary should have of the candidate", example = "string")
    private String salary;

    @Schema(name = "experience", description = "experience should have of the candidate", example = "5 year")
    private String experience;

    @Schema(name = "level", description = "skills should have of the candidate", example = "Senior")
    private String level;

    @Schema(name = "skills", description = "skills should have of the candidate", example = "Java, AWS, Angular")
    private String skills;

    @Schema(name = "description", description = "experience should have of the candidate", example = "string")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePublication;

    @Schema(name = "source", description = "source where the vacancy was launched ", example = "workday")
    private String source;

    @Schema(name = "roleId",description = "Id of rol Id",example = "1")
    private Long roleId;

    @Schema(name = "roleName",description = "Name of rol", example = "string")
    private String roleName;

    @Schema(name = "jobProfileId",description = "Id of job profile", example = "1")
    private Long jobProfileId;

    @Schema(name = "jobProfileName",description = "Name of job profile", example = "string")
    private String jobProfileName;

    @Schema(name = "originId",description = "Id of rol Id",example = "1")
    private Long originId;

    @Schema(name = "originName",description = "Name of rol", example = "string")
    private String originName;

}
