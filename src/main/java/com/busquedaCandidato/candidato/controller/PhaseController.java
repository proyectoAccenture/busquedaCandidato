package com.busquedaCandidato.candidato.controller;

import java.util.List;
import java.util.Optional;

import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.busquedaCandidato.candidato.dto.request.PhaseRequestDto;
import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.service.PhaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/phase")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseService phaseService;

    @Operation(summary = "Get a phase by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phase found",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhaseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Phase not found", content = @Content)
    })
     @GetMapping("/{id}")
    public ResponseEntity<PhaseResponseDto> getState(@PathVariable Long id){
        Optional<PhaseResponseDto> phaseOptional = phaseService.getPhase(id);
        if (phaseOptional.isPresent()) {
            return ResponseEntity.ok(phaseOptional.get());
        } else {
            throw new EntityNotFoundException("Phase not found");
        }
    }
    @Operation(summary = "Get all the phase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All phase returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StateResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<PhaseResponseDto>> getAllState(){
        try{
            List<PhaseResponseDto> phase = phaseService.getAllPhase();
            return ResponseEntity.ok(phase);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Add a new phase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Phase created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Phase already exists", content = @Content)
    })
     @PostMapping("/")
    public ResponseEntity<PhaseResponseDto> savePhase(@Valid @RequestBody PhaseRequestDto phaseRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(phaseService.savePhase(phaseRequestDto));
    }

    @Operation(summary = "Update an existing phase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phase updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Phase not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PhaseResponseDto> updatePhase(@Valid @PathVariable Long id, @RequestBody PhaseRequestDto phaseRequestDto){
        Optional<PhaseResponseDto> phaseOptional = phaseService.updatePhase(id, phaseRequestDto);
        if (phaseOptional.isPresent()) {
            return ResponseEntity.ok(phaseOptional.get());
        } else {
            throw new EntityNotFoundException("Phase not found");
        }
    }

    @Operation(summary = "Delete a phase by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phase deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Phase not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable Long id){
        boolean isDeleted = phaseService.deletePhase(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Phase not found");
        }
    }






}
