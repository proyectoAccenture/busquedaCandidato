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

    @Schema(name = "contract", description = "Contract of the candidate")
    private String contract;

    @Schema(name = "salary", description = "Salary should have of the candidate")
    private String salary;

    @Schema(name = "experience", description = "Experience should have of the candidate")
    private String experience;

    @Schema(name = "level", description = "Level should have of the candidate")
    private String level;

    @Schema(name = "skills", description = "Skills should have of the candidate")
    private String skills;

    @Schema(name = "description", description = "Description should have of the candidate")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePublication;

    @Schema(name = "source", description = "source where the vacancy was launched ")
    private String source;

    @Schema(name = "roleId",description = "Id of role")
    private Long roleId;

    @Schema(name = "roleName",description = "Name of rol")
    private String roleName;

    @Schema(name = "jobProfileId",description = "Id of job profile")
    private Long jobProfileId;

    @Schema(name = "jobProfileName",description = "Name of job profile")
    private String jobProfileName;

    @Schema(name = "originId",description = "Id of rol Id")
    private Long originId;

    @Schema(name = "originName",description = "Name of rol")
    private String originName;

}
