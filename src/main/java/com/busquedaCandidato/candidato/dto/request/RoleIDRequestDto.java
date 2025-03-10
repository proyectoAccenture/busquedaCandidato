package com.busquedaCandidato.candidato.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "RolIDRequest", description = "Model representing a RolID in the database")
@Data
@NoArgsConstructor
public class RoleIDRequestDto {
    @Schema(name = "name",description = "Name of the Role ID", example = "string")
    @NotNull(message = "Role ID cannot be null")
    @Size(min = 1, max = 100, message = "The Role ID name must be greater than 0 and less than 100")
    private String name;
}
