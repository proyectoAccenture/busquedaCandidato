package com.busquedaCandidato.candidato.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "CandidateRequest", description = "Model represent a candidate on database")
public class CandidateRequestDto {

    @Schema(name = "name", description = "Name of the candidate", example = "string")
    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 100, message = "The name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The name must only contain letters and spaces")
    private String name;

    @Schema(name = "lastName", description = "Name of the candidate", example = "string")
    @NotBlank(message = "lastName cannot be blank")
    @Size(min = 1, max = 100, message = "The last name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The last name must only contain letters and spaces")
    private String lastName;

    @Schema(name = "card",description = "card of the candidate", example = "1000234567")
    @NotNull(message = "card cannot be null")
    @Digits(integer = 10, fraction = 0, message = "card must have exactly 10 digits")
    private Long card;

    @Schema(name = "birthdate", description = "birthdate of the candidate", example = "2004-01-01")
    @NotNull(message = "birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
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
    @NotBlank(message = "city cannot be blank")
    @Size(min = 1, max = 100, message = "The city name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "The city name must only contain letters and spaces")
    private String city;

    @Schema(name = "email", description = "city of the candidate", example = "string")
    @NotBlank(message = "email cannot be blank")
    @Size(max = 150, message = "The email must be less than 150 characters")
    @Email(message = "Invalid email format")
    private String email;
}
