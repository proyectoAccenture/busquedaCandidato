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

    @Schema(name = "salaryAspiration", description = "Expected salary", example = "1.200.000")
    @NotNull(message = "salary cannot be null")
    @Size(min = 1, max = 15, message = "The salary must be between 1 and 15 characters")
    @Pattern(regexp = "^(\\d{1,3}(\\.\\d{3})*|\\d+)(,\\d{1,2})?$",
            message = "The salary must be a valid number with thousands separated by dots and optional decimals (e.g., 1.200.000, 500000, 1.234.567,89)")
    private String salaryAspiration;

    @Schema(name = "vacancyCompanyId",description = "Id of tha vacancy",example = "1")
    @NotNull(message = "vacancyCompanyId cannot be null")
    private Long vacancyCompanyId;

    @Schema(name = "candidateId",description = "Id of candidate that appliqued", example = "1")
    @NotNull(message = "candidateId cannot be null")
    private Long candidateId;

    @Schema(name = "status", description = "Status of the postulation (true if active, false if closed)", example = "false")
    private Boolean status = false;
}
