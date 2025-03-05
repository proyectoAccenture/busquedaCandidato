package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "VacancyCompanyRequest", description = "Model represent a vacancy of the candidate on database")
public class VacancyCompanyRequestDto {

    @Schema(name = "contract", description = "contract of the candidate", example = "string")
    @NotNull(message = "contract cannot be null")
    @Size(min = 1, max = 100, message = "The contract must be greater than 0 and less than 100")
    private String contract;

    @Schema(name = "salary", description = "salary should have of the candidate", example = "string")
    @NotNull(message = "salary cannot be null")
    @Size(min = 1, max = 100, message = "The name must be greater than 0 and less than 100")
    @Pattern(regexp = "^[0-9]{1,3}(\\.[0-9]{3})*$", message = "The salary must be a valid number with thousands separated by dots (e.g., 300.000, 1.234.567)")
    private String salary;

    @Schema(name = "experience", description = "experience should have of the candidate", example = "5 year")
    @NotNull(message = "experience cannot be null")
    @Size(min = 1, max = 100, message = "The experience must be greater than 0 and less than 100")
    private String experience;

    @Schema(name = "level", description = "skills should have of the candidate", example = "Senior")
    @NotNull(message = "level cannot be null")
    @Size(min = 1, max = 100, message = "The skills must be greater than 0 and less than 100")
    private String level;

    @Schema(name = "skills", description = "skills should have of the candidate", example = "Java, AWS, Angular")
    @NotNull(message = "skills cannot be null")
    @Size(min = 1, max = 100, message = "The skills must be greater than 0 and less than 100")
    private String skills;

    @Schema(name = "description", description = "experience should have of the candidate", example = "string")
    @NotNull(message = "description cannot be null")
    @Size(min = 1, max = 100, message = "The experience must be greater than 0 and less than 100")
    private String description;

    @Schema(name = "datePublication", description = "datePublication of the candidate", example = "2025-03-01")
    @NotNull(message = "datePublication cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePublication;

    @Schema(name = "source", description = "source where the vacancy was launched ", example = "workday")
    @NotNull(message = "source cannot be null")
    @Size(min = 1, max = 100, message = "The source must be greater than 0 and less than 100")
    private String source;

    @Schema(name = "role",description = "Id of roleId",example = "1")
    @NotNull(message = "roleId cannot be null")
    private Long role;

    @Schema(name = "jobProfile",description = "Id of origin", example = "1")
    @NotNull(message = "jobProfile cannot be null")
    private Long jobProfile;
}
