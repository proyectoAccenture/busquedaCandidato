package com.candidateSearch.searching.dto.request;

import com.candidateSearch.searching.utility.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

    @Schema(name = "status", description = "Status of process = 'ACTIVE', 'INACTIVE' ", example = "ACTIVE")
    @NotNull(message = "Status cannot be null")
    private Status status;

    @Schema(name = "roleId",description = "Id of role",example = "1")
    @NotNull(message = "roleId cannot be null")
    private Long roleId;

    @Schema(name = "candidateId",description = "Id of candidate that appliqued", example = "1")
    @NotNull(message = "candidateId cannot be null")
    private Long candidateId;
}
