package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.service.ProcessService;
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
@RequestMapping("api/process")
@RequiredArgsConstructor
public class ProcessController {
    private final ProcessService processService;

    @Operation(summary = "Get a candidate status history by their Number of postulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProcessEntity found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProcessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "ProcessEntity not found", content = @Content)
    })
    @GetMapping("/candidate/{id}")
    public ResponseEntity<ProcessResponseDto> getProcessByIdPostulation(@PathVariable Long id){
        return processService.getProcesByIdCandidate(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a candidate status history by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProcessEntity found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProcessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "ProcessEntity not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProcessResponseDto> getByIdCandidateStatusHistory(@PathVariable Long id){
        return processService.getByIdProcess(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProcessResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<ProcessResponseDto>> getAllCandidateStatusHistory(){
        List<ProcessResponseDto> states = processService.getAllProcess();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ProcessEntity created", content = @Content),
            @ApiResponse(responseCode = "409", description = "ProcessEntity already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<ProcessResponseDto> saveCandidateStatusHistory(@Valid @RequestBody ProcessRequestDto processRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(processService.saveProcess(processRequestDto));
    }

    @Operation(summary = "Update an existing candidate status history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProcessResponseDto> updateCandidateStatusHistory(@Valid @PathVariable Long id, @RequestBody ProcessRequestDto processRequestDto){
        return processService.updateProcess(id, processRequestDto)
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
        return processService.deleteProcess(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

