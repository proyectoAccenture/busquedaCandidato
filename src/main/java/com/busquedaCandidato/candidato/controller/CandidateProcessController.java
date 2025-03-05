package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateProcessResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.service.CandidateProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate_process")
@RequiredArgsConstructor
public class CandidateProcessController {

    private final CandidateProcessService candidateProcessService;

    @Operation(summary = "Add a new phase to process ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Phase created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Phase already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CandidateProcessResponseDto> saveCandidateProcess(@Valid @RequestBody CandidateProcessRequestDto candidateProcessRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateProcessService.addPhaseToProcess(candidateProcessRequestDto));
    }

    @Operation(summary = "Get a phase by their number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phase found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateProcessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "phase not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CandidateProcessResponseDto> getCandidateProcess(@PathVariable Long id) {
        CandidateProcessResponseDto candidateProcessResponseDto = candidateProcessService.getCandidateProcessById(id);

        if (candidateProcessResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateProcessResponseDto);
    }

    @Operation(summary = "Get all the candidate process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All history process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateProcessResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<CandidateProcessResponseDto>> getAllCandidateProcess(){
        List<CandidateProcessResponseDto> states = candidateProcessService.getAllCandidateProcess();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Update an existing candidate phase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CandidateProcessResponseDto> updateCandidateProcess(@Valid @PathVariable Long id, @RequestBody CandidateProcessRequestUpdateDto candidateProcessRequestUpdateDto){
        return candidateProcessService.updateCandidateProcess(id, candidateProcessRequestUpdateDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a candidate phase by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "candidate phase deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "candidate phase not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id){
        return candidateProcessService.deleteCandidateProcess(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}