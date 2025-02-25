package com.busquedaCandidato.candidato.controller;


import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
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
@RequestMapping("/state")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @Operation(summary = "Get a state by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State found",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })


    @GetMapping("/{id}")
    public ResponseEntity<StateResponseDto> getState(@PathVariable Long id){
        return stateService.getState(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All state returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StateResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<StateResponseDto>> getAllState(){
        List<StateResponseDto> states = stateService.getAllState();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "State created", content = @Content),
            @ApiResponse(responseCode = "409", description = "State already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<StateResponseDto> saveState(@Valid @RequestBody StateRequestDto stateRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(stateService.saveState(stateRequestDto));
    }

    @Operation(summary = "Update an existing state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<StateResponseDto> updateState(@Valid @PathVariable Long id, @RequestBody StateRequestDto stateRequestDto){
        return stateService.updateState(id, stateRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a state by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id){
        return stateService.deleteState(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
