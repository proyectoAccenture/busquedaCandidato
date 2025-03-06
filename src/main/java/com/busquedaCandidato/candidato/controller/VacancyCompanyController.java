package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.VacancyCompanyRequestDto;
import com.busquedaCandidato.candidato.dto.response.VacancyCompanyResponseDto;
import com.busquedaCandidato.candidato.service.VacancyCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancy_company")
@RequiredArgsConstructor
public class VacancyCompanyController {

    private final VacancyCompanyService vacancyCompanyService;

    @Operation(summary = "Get a vacancy by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacancyCompanyResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<VacancyCompanyResponseDto> getVacancy(@PathVariable Long id){
        return vacancyCompanyService.getVacancyCompany(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All vacancy returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = VacancyCompanyResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<VacancyCompanyResponseDto>> getAllVacancy(){
        List<VacancyCompanyResponseDto> states = vacancyCompanyService.getAllVacancyCompany();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacancy created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Vacancy already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<VacancyCompanyResponseDto> saveVacancy(@Valid @RequestBody VacancyCompanyRequestDto vacancyCompanyRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(vacancyCompanyService.saveVacancyCompany(vacancyCompanyRequestDto));
    }

    @Operation(summary = "Update an existing vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vacancy not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<VacancyCompanyResponseDto> updateCandidate(@Valid @PathVariable Long id, @RequestBody VacancyCompanyRequestDto vacancyCompanyRequestDto){
        return vacancyCompanyService.updateVacancyCompany(id, vacancyCompanyRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a vacancy by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vacancy not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id){
        return vacancyCompanyService.deleteVacancyCompany(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }
}
