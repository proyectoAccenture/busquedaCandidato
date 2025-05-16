package com.candidateSearch.searching.dto.response;

import com.candidateSearch.searching.utility.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(name = "RoleResponse", description = "Model represent a vacancy on database")
public class RoleResponseDto {

    @Schema(name = "id", defaultValue = "1", description = "Unique Id of origin on database")
    private final Long id;

    @Schema(name = "nameRole",description = "Role id name")
    private String nameRole;

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

    @Schema(name = "experience", description = "Experience of the candidate")
    private String experience;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignmentTime;

    @Schema(name = "status", description = "Status of the role (true if active, false if closed)", example = "false")
    private Status status;

    @Schema(name = "jobProfileId",description = "Id of job profile")
    private Long jobProfileId;

    @Schema(name = "jobProfileName",description = "Name of job profile")
    private String jobProfileName;

    @Schema(name = "originId",description = "Id of origin")
    private Long originId;

    @Schema(name = "originName",description = "Name of origin")
    private String originName;
}
