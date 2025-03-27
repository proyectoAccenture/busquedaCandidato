package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "VacancyCompanyResponse", description = "Model represent a vacancy on database")
public class VacancyCompanyResponseDto {

    @Schema(name = "id", defaultValue = "1", description = "Unique Id of origin on database")
    private final Long id;

    @Schema(name = "description", description = "Description should have of the candidate")
    private String description;

    @Schema(name = "contract", description = "Contract of the candidate")
    private String contract;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###")
    private Long salary;

    @Schema(name = "level", description = "level")
    private Integer level;

    @Schema(name = "seniority", description = "seniority of the candidate")
    private String seniority;

    @Schema(name = "skills", description = "Skills should have of the candidate")
    private String skills;

    @Schema(name = "assignmentTime", description = "Assignment of the candidate")
    private String assignmentTime;

    @Schema(name = "roleId",description = "Id of role")
    private Long roleId;

    @Schema(name = "roleName",description = "Name of role")
    private String roleName;

    @Schema(name = "jobProfileId",description = "Id of job profile")
    private Long jobProfileId;

    @Schema(name = "jobProfileName",description = "Name of job profile")
    private String jobProfileName;

    @Schema(name = "originId",description = "Id of origin")
    private Long originId;

    @Schema(name = "originName",description = "Name of origin")
    private String originName;
}
