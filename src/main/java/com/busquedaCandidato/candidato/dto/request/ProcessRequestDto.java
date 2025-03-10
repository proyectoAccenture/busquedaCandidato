package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "ProcessRequest", description = "Model represent a candidate process on database")
public class ProcessRequestDto {

    @Schema(name = "postulationId",description = "Id of postulation", example = "1")
    @NotNull(message = "postulationId cannot be null")
    private Long postulationId;

    @Schema(name = "description", description = "Description about candidate Phases", example = "This process is...")
    @NotNull(message = "description cannot be null")
    private String description;

    @Schema(name = "assignedDate", description = "Date when the candidate is assigned", example = "2025-01-01")
    @NotNull(message = "Assigned date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
}
