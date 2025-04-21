package com.busquedaCandidato.candidato.dto.response;

import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(name = "RoleIDResponse", description = "Model representing a Role ID in the database")
@Data
public class RoleIDResponseDto {
    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the Role ID in the database")
    private Long id;

    @Schema(name = "name",description = "Role id name")
    private String name;

    @Schema(name = "description", description = "Role description")
    private String description;

    @Schema(name = "vacancyCompanyId",description = "Vacancy id")
    private Long vacancyCompanyId;

    @Schema(name = "vacancyCompanyName", description = "Vacancy name")
    private String vacancyCompanyName;


}
