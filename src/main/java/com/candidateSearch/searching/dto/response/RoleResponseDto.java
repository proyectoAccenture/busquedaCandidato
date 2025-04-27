package com.candidateSearch.searching.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "RoleIDResponse", description = "Model representing a Role ID in the database")
@Data
public class RoleResponseDto {

    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the Role ID in the database")
    private Long id;

    @Schema(name = "name",description = "Role id name")
    private String name;

    @Schema(name = "description", description = "Role description")
    private String description;

    @Schema(name = "companyVacancyId",description = "Vacancy id")
    private Long companyVacancyId;

    @Schema(name = "companyVacancyName", description = "Vacancy name")
    private String companyVacancyName;
}
