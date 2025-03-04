package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.service.ProcessService;
import com.busquedaCandidato.candidato.service.StateService;
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
public class ProcessController {
    private final ProcessService processService;

    @Operation(summary = "Get a process by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Process found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Process not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProcessResponseDto> getProcess(@PathVariable Long id){
        return processService.getProcess(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProcessResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<ProcessResponseDto>> getAllProcess(){
        List<ProcessResponseDto> states = processService.getAllProcess();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Process created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Process already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<ProcessResponseDto> saveProcess(@Valid @RequestBody ProcessRequestDto processRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(processService.saveProcess(processRequestDto));
    }

    @Operation(summary = "Update an existing process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProcessResponseDto> updateProcess(@Valid @PathVariable Long id, @RequestBody ProcessRequestDto processRequestDto){
        return processService.updateProcess(id, processRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a process by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProces(@PathVariable Long id){
        return processService.deleteProcess(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
