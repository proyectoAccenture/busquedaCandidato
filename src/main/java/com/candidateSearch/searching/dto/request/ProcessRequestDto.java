package com.candidateSearch.searching.dto.request;

import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.dto.request.validation.date.DateWithinRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "ProcessRequest", description = "Model represent a candidate process on database")
public class ProcessRequestDto {

    @Schema(name = "postulationId",description = "Id of postulation", example = "1")
    @NotNull(message = "postulationId cannot be null")
    @Min(value = 1, message = "postulationId must be greater than 0")
    private Long postulationId;

    @Schema(name = "description", description = "Description about candidate state", example = "This process is...")
    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 10 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 .,!?-]+$", message = "Description contains invalid characters")
    private String description;

    @Schema(name = "assignedDate", description = "Date when the candidate is assigned", example = "2025-01-01")
    @NotNull(message = "Assigned date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateWithinRange
    private LocalDate assignedDate;

    @Schema(name = "status", description = "Status of process = 'ACTIVE', 'INACTIVE' ", example = "ACTIVE")
    @NotNull(message = "Status cannot be null")
    private Status status;
}
