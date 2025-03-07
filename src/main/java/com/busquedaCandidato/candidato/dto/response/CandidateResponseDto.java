package com.busquedaCandidato.candidato.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

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
}
