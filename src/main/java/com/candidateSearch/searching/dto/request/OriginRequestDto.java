package com.candidateSearch.searching.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "OriginRequest", description = "Model represent a origin on database")
@Data
@NoArgsConstructor
public class OriginRequestDto {

    @Schema(name = "name", description = "Name of the origin", example = "string")
    @NotBlank(message = "The name field cannot be blank")
    @Size(min = 1, max = 250, message = "The name field must be between 1 and 250 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The name field must not contain special characters or numbers")
    private String name;
}
