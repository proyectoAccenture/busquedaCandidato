package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.CompanyVacancyRequestDto;
import com.candidateSearch.searching.dto.response.CompanyVacancyResponseDto;
import com.candidateSearch.searching.service.CompanyVacancyService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/company_vacancy")
@RequiredArgsConstructor
public class CompanyVacancyController {

    private final CompanyVacancyService companyVacancyService;

    @Operation(summary = "Get a vacancy by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyVacancyResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyVacancyResponseDto> getBtIdVacancy(@PathVariable Long id){
        CompanyVacancyResponseDto companyVacancyResponseDto = companyVacancyService.getVacancyCompany(id);
        return ResponseEntity.ok(companyVacancyResponseDto);
    }

    @Operation(summary = "Get all the vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All vacancy returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompanyVacancyResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<CompanyVacancyResponseDto>> getAllVacancy(){
        List<CompanyVacancyResponseDto> states = companyVacancyService.getAllVacancyCompany();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacancy created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Vacancy already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CompanyVacancyResponseDto> saveVacancy(@Valid @RequestBody CompanyVacancyRequestDto companyVacancyRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyVacancyService.saveVacancyCompany(companyVacancyRequestDto));
    }

    @Operation(summary = "Update an existing vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vacancy not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyVacancyResponseDto> updateVacancy(@Valid @PathVariable Long id, @Valid @RequestBody CompanyVacancyRequestDto companyVacancyRequestDto){
        return companyVacancyService.updateVacancyCompany(id, companyVacancyRequestDto)
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
        companyVacancyService.deleteVacancyCompany(id);
        return ResponseEntity.noContent().build();
    }
}
