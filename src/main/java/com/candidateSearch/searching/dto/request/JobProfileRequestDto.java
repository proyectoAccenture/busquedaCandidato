package com.candidateSearch.searching.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "JobProfileRequest", description = "Model representing a job profile in the database")
@Data
@NoArgsConstructor
public class JobProfileRequestDto {

    @Schema(name = "name",description = "Name of the job profile", example = "string")
    @NotBlank(message = "The name field cannot be blank")
    @Size(min = 1, max = 50, message = "The name field  must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The name field  must only contain letters and spaces")
    private String name;
}
