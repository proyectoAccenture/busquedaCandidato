package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidateStatusHistoryRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateStatusHistoryResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.service.CandidateStatusHistoryService;
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
@RequestMapping("/process")
@RequiredArgsConstructor
public class CandidateStatusHistoryController {
    private final CandidateStatusHistoryService candidateStatusHistoryService;

    @Operation(summary = "Get a candidate status history by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Process found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Process not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CandidateStatusHistoryResponseDto> getByIdCandidateStatusHistory(@PathVariable Long id){
        return candidateStatusHistoryService.getByIdCandidateStatusHistory(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateStatusHistoryResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<CandidateStatusHistoryResponseDto>> getAllAllCandidateStatusHistory(){
        List<CandidateStatusHistoryResponseDto> states = candidateStatusHistoryService.getAllCandidateStatusHistory();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Process created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Process already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CandidateStatusHistoryResponseDto> saveCandidateStatusHistory(@Valid @RequestBody CandidateStatusHistoryRequestDto candidateStatusHistoryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateStatusHistoryService.saveCandidateStatusHistory(candidateStatusHistoryRequestDto));
    }

    @Operation(summary = "Update an existing candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CandidateStatusHistoryResponseDto> updateCandidateStatusHistory(@Valid @PathVariable Long id, @RequestBody CandidateStatusHistoryRequestDto candidateStatusHistoryRequestDto){
        return candidateStatusHistoryService.updateCandidateStatusHistory(id, candidateStatusHistoryRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a candidate status history by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidateStatusHistory(@PathVariable Long id){
        return candidateStatusHistoryService.deleteCandidateStatusHistory(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
