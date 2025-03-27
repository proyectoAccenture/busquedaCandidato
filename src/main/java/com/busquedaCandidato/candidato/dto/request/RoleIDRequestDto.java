package com.busquedaCandidato.candidato.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "RolIDRequest", description = "Model representing a RolID in the database")
@Data
@NoArgsConstructor
public class RoleIDRequestDto {
    @Schema(name = "name",description = "Name of the Role ID", example = "string")
    @NotBlank(message = "Role ID cannot be blank")
    @Size(min = 1, max = 100, message = "The Role name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s]+$", message = "The Role name must only contain letters, numbers, and spaces")
    private String name;
}
