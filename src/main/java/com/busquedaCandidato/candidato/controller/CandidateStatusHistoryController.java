package com.busquedaCandidato.candidato.controller;

<<<<<<< HEAD:src/main/java/com/busquedaCandidato/candidato/controller/CandidateStatusHistoryController.java
import com.busquedaCandidato.candidato.dto.request.CandidateStatusHistoryRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateStatusHistoryResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.service.CandidateStatusHistoryService;
=======
import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityNotFoundException;
import com.busquedaCandidato.candidato.service.ProcessService;
import com.busquedaCandidato.candidato.service.StateService;
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594:src/main/java/com/busquedaCandidato/candidato/controller/ProcessController.java
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
import java.util.Optional;

@RestController
@RequestMapping("/api/process")
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
<<<<<<< HEAD:src/main/java/com/busquedaCandidato/candidato/controller/CandidateStatusHistoryController.java
    public ResponseEntity<CandidateStatusHistoryResponseDto> getByIdCandidateStatusHistory(@PathVariable Long id){
        return candidateStatusHistoryService.getByIdCandidateStatusHistory(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
=======
    public ResponseEntity<ProcessResponseDto> getProcess(@PathVariable Long id){
        Optional<ProcessResponseDto> processOptional = processService.getProcess(id);
        if (processOptional.isPresent()) {
            return ResponseEntity.ok(processOptional.get());
        } else {
            throw new EntityNotFoundException("Process not found");
        }
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594:src/main/java/com/busquedaCandidato/candidato/controller/ProcessController.java
    }

    @Operation(summary = "Get all the candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateStatusHistoryResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
<<<<<<< HEAD:src/main/java/com/busquedaCandidato/candidato/controller/CandidateStatusHistoryController.java
    public ResponseEntity<List<CandidateStatusHistoryResponseDto>> getAllAllCandidateStatusHistory(){
        List<CandidateStatusHistoryResponseDto> states = candidateStatusHistoryService.getAllCandidateStatusHistory();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
=======
    public ResponseEntity<List<ProcessResponseDto>> getAllProcess(){
        try{
            List<ProcessResponseDto> process = processService.getAllProcess();
            return ResponseEntity.ok(process);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594:src/main/java/com/busquedaCandidato/candidato/controller/ProcessController.java
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
<<<<<<< HEAD:src/main/java/com/busquedaCandidato/candidato/controller/CandidateStatusHistoryController.java
    public ResponseEntity<CandidateStatusHistoryResponseDto> updateCandidateStatusHistory(@Valid @PathVariable Long id, @RequestBody CandidateStatusHistoryRequestDto candidateStatusHistoryRequestDto){
        return candidateStatusHistoryService.updateCandidateStatusHistory(id, candidateStatusHistoryRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
=======
    public ResponseEntity<ProcessResponseDto> updateProcess(@Valid @PathVariable Long id, @RequestBody ProcessRequestDto processRequestDto){
        Optional<ProcessResponseDto> processOptional = processService.updateProcess(id, processRequestDto);
        if (processOptional.isPresent()) {
            return ResponseEntity.ok(processOptional.get());
        } else {
            throw new EntityNotFoundException("Process not found");
        }
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594:src/main/java/com/busquedaCandidato/candidato/controller/ProcessController.java
    }

    @Operation(summary = "Delete a candidate status history by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @DeleteMapping("/{id}")
<<<<<<< HEAD:src/main/java/com/busquedaCandidato/candidato/controller/CandidateStatusHistoryController.java
    public ResponseEntity<Void> deleteCandidateStatusHistory(@PathVariable Long id){
        return candidateStatusHistoryService.deleteCandidateStatusHistory(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
=======
    public ResponseEntity<Void> deleteProcess(@PathVariable Long id){
        boolean isDeleted = processService.deleteProcess(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Process not found");
        }
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594:src/main/java/com/busquedaCandidato/candidato/controller/ProcessController.java
    }
}
