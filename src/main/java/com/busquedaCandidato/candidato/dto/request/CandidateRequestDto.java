package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateRequest", description = "Model represent a candidate on database")
public class CandidateRequestDto {

    @Schema(name = "name", description = "Name of the candidate", example = "string")
    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 100, message = "The name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The name must only contain letters and spaces")
    private String name;

    @Schema(name = "lastName", description = "Name of the candidate", example = "string")
    @NotBlank(message = "lastName cannot be blank")
    @Size(min = 1, max = 100, message = "The last name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The last name must only contain letters and spaces")
    private String lastName;

    @Schema(name = "card",description = "card of the candidate", example = "1000234567")
    @NotNull(message = "card cannot be null")
    @Digits(integer = 10, fraction = 0, message = "card must have exactly 10 digits")
    private Long card;

    @Schema(name = "phone",description = "phone of the candidate", example = "3002004050")
    @NotNull(message = "phone cannot be null")
    @Digits(integer = 10, fraction = 0, message = "phone must have exactly 10 digits")
    private Long phone;

    @Schema(name = "city", description = "city of the candidate", example = "string")
    @NotBlank(message = "city cannot be blank")
    @Size(min = 1, max = 100, message = "The city name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The city name must only contain letters and spaces")
    private String city;

    @Schema(name = "email", description = "email of the candidate", example = "string@gmail.com")
    @NotBlank(message = "email cannot be blank")
    @Size(max = 150, message = "The email must be less than 150 characters")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(name = "birthdate", description = "birthdate of the candidate", example = "2004-01-01")
    @NotNull(message = "birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Schema(name = "source", description = "source where the vacancy was launched ", example = "string")
    @NotBlank(message = "Source cannot be blank")
    @Size(min = 1, max = 100, message = "The source must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$", message = "The source must not contain special characters except spaces")
    private String source;

    @Schema(name = "skills", description = "skills should have of the candidate", example = "string")
    @NotBlank(message = "Skills cannot be blank")
    @Size(min = 1, max = 100, message = "The skills must be between 1 and 100 characters")
    private String skills;

    @Schema(name= "yearsExperience",description = "years Experience", example = "1")
    @NotNull(message = "Years experience scale cannot be null")
    @Size(min = 1, max = 100, message = "The experience must be between 1 and 100 characters")
    private String yearsExperience;

    @Schema(name = "workExperience", description = "work experience should have of the candidate", example = "string")
    @NotBlank(message = "Work Experience cannot be blank")
    @Size(min = 1, max = 100, message = "The experience must be between 1 and 100 characters")
    private String workExperience;

    @Schema(name = "seniority", description = "seniority should have of the candidate", example = "string")
    @NotBlank(message = "Seniority cannot be blank")
    @Size(min = 1, max = 100, message = "The level must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The level must not contain special characters or numbers")
    private String seniority;

    @Schema(name = "salaryAspiration", description = "Expected salary", example = "3000000")
    @NotNull(message = "Salary Aspiration cannot be null")
    @Min(value = 1, message = "The salary aspiration must be at least 1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###")
    private Long salaryAspiration;

    @Schema(name= "level",description = "level", example = "13")
    @NotNull(message = "Level scale cannot be null")
    @Min(value = 1, message = "The level must be greater 1")
    @Max(value = 13, message = "The level must be least 1")
    private Integer level;

    @Schema(name = "datePresentation", description = "date Presentation of the candidate", example = "2025-01-01")
    @NotNull(message = "datePresentation cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePresentation;

    @Schema(name = "origin",description = "Id of origin", example = "1")
    @NotNull(message = "origin cannot be null")
    private Long origin;

    @Schema(name = "jobProfile",description = "Id of jobProfile", example = "1")
    @NotNull(message = "jobProfile cannot be null")
    private Long jobProfile;
}