package com.candidateSearch.searching.dto.request;

import com.candidateSearch.searching.dto.request.validation.date.DateWithinRange;
import com.candidateSearch.searching.dto.request.validation.date.NotBeforeThreeMonths;
import com.candidateSearch.searching.entity.utility.Status;
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
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "RoleRequest", description = "Model represent a vacancy of the candidate on database")
public class RoleRequestDto {

    @Schema(name = "nameRole",description = "Name of the Role ID", example = "string")
    @NotBlank(message = "The nameRole field cannot be blank")
    @Size(min = 1, max = 100, message = "The nameRole field must be between 1 and 100 characters")
    private String nameRole;

    @Schema(name = "description", description = "experience should have of the candidate", example = "string")
    @NotBlank(message = "The description field cannot be blank")
    @Size(min = 1, max = 500, message = "The description field must be between 1 and 500 characters")
    private String description;

    @Schema(name = "contract", description = "contract of the candidate", example = "string")
    @NotBlank(message = "The contract field type cannot be blank")
    @Size(min = 1, max = 100, message = "The contract field type must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The contract field type must not contain special characters or numbers")
    private String contract;

    @Schema(name = "salary", description = "Expected salary", example = "3000000")
    @NotNull(message = "The salary field cannot be null")
    @Min(value = 1_423_500, message = "The salary field is lower to SMMLV")
    @Max(value = 40_000_000, message = "The salary field is highest to 40000000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###")
    private Long salary;

    @Schema(name= "level",description = "level", example = "13")
    @NotNull(message = "The level field cannot be null")
    @Min(value = 1, message = "The level field must be greater than or equal to 1")
    @Max(value = 13, message = "The level field must be less than 14")
    private Integer level;

    @Schema(name = "seniority", description = "seniority should have of the candidate", example = "string")
    @NotBlank(message = "The seniority field cannot be blank")
    @Size(min = 1, max = 100, message = "The seniority field must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The seniority field must not contain special characters or numbers")
    private String seniority;

    @Schema(name = "skills", description = "skills should have of the candidate", example = "string")
    @NotBlank(message = "The skills field cannot be blank")
    @Size(min = 1, max = 100, message = "The skills field must be between 1 and 100 characters")
    private String skills;

    @Schema(name= "experience",description = "Experience candidate", example = "string")
    @NotNull(message = "The experience field cannot be null")
    @Size(min = 1, max = 100, message = "The experience field must be between 1 and 100 characters")
    private String experience;

    @Schema(name = "assignmentTime", description = "source where the vacancy was launched ", example = "2025-03-01")
    @NotNull(message = "The assignment field cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotBeforeThreeMonths(message = "The assignmentTime field cannot be more than 3 months in the past.")
    private LocalDate assignmentTime;

    @Schema(name = "status", description = "Status of process = 'ACTIVE', 'INACTIVE' ", example = "ACTIVE")
    @NotNull(message = "The status field cannot be null")
    private Status status;

    @Schema(name = "jobProfile",description = "Id of jobProfile", example = "1")
    @NotNull(message = "The jobProfile field cannot be null")
    private Long jobProfile;

    @Schema(name = "origin",description = "Id of origin", example = "1")
    @NotNull(message = "The origin field cannot be null")
    private Long origin;
}