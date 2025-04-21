package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "VacancyCompanyRequest", description = "Model represent a vacancy of the candidate on database")
public class VacancyCompanyRequestDto {

    @Schema(name = "description", description = "experience should have of the candidate", example = "string")
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 500, message = "The description must be between 1 and 500 characters")
    private String description;

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

    @Schema(name= "level",description = "level", example = "13")
    @NotNull(message = "Level scale cannot be null")
    @Min(value = 1, message = "The level must be greater 1")
    @Max(value = 13, message = "The level must be least 1")
    private Integer level;

    @Schema(name = "seniority", description = "seniority should have of the candidate", example = "string")
    @NotBlank(message = "Seniority cannot be blank")
    @Size(min = 1, max = 100, message = "The level must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The level must not contain special characters or numbers")
    private String seniority;

    @Schema(name = "skills", description = "skills should have of the candidate", example = "string")
    @NotBlank(message = "Skills cannot be blank")
    @Size(min = 1, max = 100, message = "The skills must be between 1 and 100 characters")
    private String skills;

    @Schema(name= "experience",description = "Experience candidate", example = "string")
    @NotNull(message = "Years experience scale cannot be null")
    @Size(min = 1, max = 100, message = "The experience must be between 1 and 100 characters")
    private String experience;

    @Schema(name = "assignmentTime", description = "source where the vacancy was launched ", example = "string")
    @NotBlank(message = "Assignment time cannot be blank")
    @Size(min = 1, max = 100, message = "The source must be between 1 and 100 characters")
    private String assignmentTime;

    @Schema(name = "jobProfile",description = "Id of jobProfile", example = "1")
    @NotNull(message = "jobProfile cannot be null")
    private Long jobProfile;

    @Schema(name = "origin",description = "Id of origin", example = "1")
    @NotNull(message = "origin cannot be null")
    private Long origin;
}
