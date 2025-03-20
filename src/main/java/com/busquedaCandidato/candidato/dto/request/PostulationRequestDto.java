package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "PostulationRequest", description = "Model represent a postulation of the candidate on database")
public class PostulationRequestDto {

    @Schema(name = "datePresentation", description = "Date when the candidate applied", example = "2025-03-01")
    @NotNull(message = "datePresentation cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datePresentation;

    @Schema(name = "salaryAspiration", description = "Expected salary", example = "3000000")
    @NotNull(message = "Salary cannot be null")
    @Min(value = 1, message = "The salary must be at least 1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###")
    private Long salaryAspiration;

    @Schema(name = "vacancyCompanyId",description = "Id of tha vacancy",example = "1")
    @NotNull(message = "vacancyCompanyId cannot be null")
    private Long vacancyCompanyId;

    @Schema(name = "candidateId",description = "Id of candidate that appliqued", example = "1")
    @NotNull(message = "candidateId cannot be null")
    private Long candidateId;
}
