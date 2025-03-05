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
@Schema(name = "PostulationRequest", description = "Model represent a postulation of the candidate on database")
public class PostulationRequestDto {

    @Schema(name = "datePresentation", description = "date in which the candidate appliqued", example = "2025-03-01")
    @NotNull(message = "datePresentation cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePresentation;

    @Schema(name = "salaryAspiration", description = "write the salary you hope to have ", example = "0.000.000")
    @NotNull(message = "salary cannot be null")
    @Size(min = 1, max = 100, message = "The name must be greater than 0 and less than 100")
    @Pattern(regexp = "^[0-9]{1,3}(\\.[0-9]{3})*$", message = "The salary must be a valid number with thousands separated by dots (e.g., 300.000, 1.234.567)")
    private String salaryAspiration;

    @Schema(name = "salaryCurrent", description = "the salary you have in this moment", example = "0.000.000")
    @Size(min = 1, max = 100, message = "The name must be greater than 0 and less than 100")
    @Pattern(regexp = "^[0-9]{1,3}(\\.[0-9]{3})*$", message = "The salary must be a valid number with thousands separated by dots (e.g., 300.000, 1.234.567)")
    private String salaryCurrent;

    @Schema(name = "experience", description = "the the experience you have", example = "5 year")
    @NotNull(message = "experience cannot be null")
    @Size(min = 1, max = 100, message = "The experience must be greater than 0 and less than 100")
    private String experience;

    @Schema(name = "level", description = "leve you have", example = "Senior")
    @NotNull(message = "level cannot be null")
    @Size(min = 1, max = 100, message = "The level must be greater than 0 and less than 100")
    private String level;

    @Schema(name = "skills", description = "skills you have", example = "Java, AWS, Angular")
    @NotNull(message = "skills cannot be null")
    @Size(min = 1, max = 100, message = "The skills must be greater than 0 and less than 100")
    private String skills;

    @Schema(name = "vacancyCompanyId",description = "Id of tha vacancy",example = "1")
    @NotNull(message = "vacancyCompanyId cannot be null")
    private Long vacancyCompanyId;

    @Schema(name = "candidateId",description = "Id of candidate that appliqued", example = "1")
    @NotNull(message = "candidateId cannot be null")
    private Long candidateId;
}
