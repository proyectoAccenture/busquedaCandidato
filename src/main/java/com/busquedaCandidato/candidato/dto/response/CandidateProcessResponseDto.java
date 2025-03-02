package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(name = "CandidateProcessResponse",description = "Model representing a candidate process in the database")
@Data
public class CandidateProcessResponseDto {
    @Schema(name = "id",defaultValue = "1", description = "Unique Id of the candidate process in the database")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
}
