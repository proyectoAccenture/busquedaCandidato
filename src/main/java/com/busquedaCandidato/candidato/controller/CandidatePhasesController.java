package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidatePhasesRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidatePhasesRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidatePhasesResponseDto;
import com.busquedaCandidato.candidato.service.CandidatePhasesService;
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
@RequestMapping("/api/candidate_phases")
@RequiredArgsConstructor
public class CandidatePhasesController {

    private final CandidatePhasesService candidatePhasesService;

    @Operation(summary = "Add a new phase to process ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Phase created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Phase already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CandidatePhasesResponseDto> saveCandidatePhases(@Valid @RequestBody CandidatePhasesRequestDto candidatePhasesRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatePhasesService.addPhaseToProcess(candidatePhasesRequestDto));
    }

    @Operation(summary = "Get a phase by their number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phase found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidatePhasesResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "phase not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CandidatePhasesResponseDto> getCandidatePhases(@PathVariable Long id) {
        CandidatePhasesResponseDto candidatePhasesResponseDto = candidatePhasesService.getCandidatePhasesById(id);
        return ResponseEntity.ok(candidatePhasesResponseDto);
    }

    @Operation(summary = "Get all the candidate process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All history process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidatePhasesResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<CandidatePhasesResponseDto>> getAllCandidatePhases(){
        List<CandidatePhasesResponseDto> states = candidatePhasesService.getAllCandidatePhases();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Update an existing candidate phase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CandidatePhasesResponseDto> updateCandidatePhases(@Valid @PathVariable Long id, @Valid @RequestBody CandidatePhasesRequestUpdateDto candidatePhasesRequestUpdateDto){
        return candidatePhasesService.updateCandidatePhases(id, candidatePhasesRequestUpdateDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a candidate phase by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "candidate phase deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "candidate phase not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidatePhases(@PathVariable Long id){
        candidatePhasesService.deleteCandidatePhases(id);
        return ResponseEntity.noContent().build();
    }
}