package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateRequest", description = "Model represent a candidate on database")
public class CandidateRequestDto {

    @Schema(name = "name", description = "Name of the candidate", example = "string")
    @NotNull(message = "name cannot be null")
    @Size(min = 1, max = 100, message = "The name must be greater than 0 and less than 100")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The name must not contain special characters or numbers")
    private String name;

    @Schema(name = "lastName", description = "Name of the candidate", example = "string")
    @NotNull(message = "lastName cannot be null")
    @Size(min = 1, max = 100, message = "The last name must be greater than 0 and less than 100")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The name must not contain special characters or numbers")
    private String lastName;

    @Schema(name = "card",description = "card of the candidate", example = "1000234567")
    @NotNull(message = "card cannot be null")
    @Digits(integer = 10, fraction = 0, message = "card must have exactly 10 digits")
    private Long card;

    @Schema(name = "birthdate", description = "birthdate of the candidate", example = "2004-01-01")
    @NotNull(message = "birthdate cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Schema(name = "registrationDate", description = "registrationDate of the candidate", example = "2025-01-01")
    @NotNull(message = "registrationDate cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @Schema(name = "phone",description = "phone of the candidate", example = "3002004050")
    @NotNull(message = "phone cannot be null")
    @Digits(integer = 10, fraction = 0, message = "phone must have exactly 10 digits")
    private Long phone;

    @Schema(name = "city", description = "city of the candidate", example = "string")
    @NotNull(message = "city cannot be null")
    @Size(min = 1, max = 100, message = "The city must be greater than 0 and less than 100")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The city must not contain special characters or numbers")
    private String city;

    @Schema(name = "email", description = "city of the candidate", example = "string")
    @NotNull(message = "email cannot be null")
    @Size(min = 1, max = 150, message = "The email must be greater than 0 and less than 100")
    private String email;
}
