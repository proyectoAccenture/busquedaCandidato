package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CandidateResponse",description = "Model representing a candidate on the database")
@Data
public class CandidateResponseDto {
    @Schema(name = "id",description = "id of the candidate")
    private Long id;

    @Schema(name = "name", description = "name of the candidate")
    private String name;

    @Schema(name = "lastName", description = "last name of the candidate")
    private String lastName;

    @Schema(name = "card",description = "id card of the candidate")
    private Long card;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @Schema(name = "phone",description = "phone of the candidate")
    private Long phone;

    @Schema(name = "city", description = "city of the candidate")
    private String city;

    @Schema(name = "email", description = "email of the candidate")
    private String email;

    // raro
    @Schema(name = "phases", description = "List of phases where the candidate is in")
    private List<CandidatePhasesResponseDto> phases;
}
