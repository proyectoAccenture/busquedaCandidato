package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "VacancyCompanyRequest", description = "Model represent a vacancy of the candidate on database")
public class VacancyCompanyRequestDto {

    @Schema(name = "contract", description = "contract of the candidate", example = "string")
    @NotBlank(message = "Contract type cannot be blank")
    @Size(min = 1, max = 100, message = "The contract type must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The contract type must not contain special characters or numbers")
    private String contract;

    @Schema(name = "salary", description = "Expected salary", example = "3000000")
    @NotNull(message = "salary cannot be null")
    @Min(value = 1, message = "The salary must be at least 1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###")
    private Long salary;

    @Schema(name = "experience", description = "experience should have of the candidate", example = "string")
    @NotBlank(message = "Experience cannot be blank")
    @Size(min = 1, max = 100, message = "The experience must be between 1 and 100 characters")
    private String experience;

    @Schema(name = "level", description = "skills should have of the candidate", example = "string")
    @NotBlank(message = "Level cannot be blank")
    @Size(min = 1, max = 100, message = "The level must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The level must not contain special characters or numbers")
    private String level;

    @Schema(name = "skills", description = "skills should have of the candidate", example = "string")
    @NotBlank(message = "Skills cannot be blank")
    @Size(min = 1, max = 100, message = "The skills must be between 1 and 100 characters")
    private String skills;

    @Schema(name = "description", description = "experience should have of the candidate", example = "string")
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 500, message = "The description must be between 1 and 500 characters")
    private String description;

    @Schema(name = "datePublication", description = "datePublication of the candidate", example = "2025-01-01")
    @NotNull(message = "datePublication cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePublication;

    @Schema(name = "source", description = "source where the vacancy was launched ", example = "string")
    @NotBlank(message = "Source cannot be blank")
    @Size(min = 1, max = 100, message = "The source must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$", message = "The source must not contain special characters except spaces")
    private String source;

    @Schema(name = "role",description = "Id of role",example = "1")
    @NotNull(message = "roleId cannot be null")
    private Long role;

    @Schema(name = "jobProfile",description = "Id of jobProfile", example = "1")
    @NotNull(message = "jobProfile cannot be null")
    private Long jobProfile;

    @Schema(name = "origin",description = "Id of origin", example = "1")
    @NotNull(message = "origin cannot be null")
    private Long origin;
}
