package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.CandidateStateRequestDto;
import com.candidateSearch.searching.dto.request.CandidateStateRequestUpdateDto;
import com.candidateSearch.searching.dto.response.CandidateStateResponseDto;
import com.candidateSearch.searching.dto.response.NextValidStatesResponseDto;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.mapper.IMapperState;
import com.candidateSearch.searching.service.CandidateStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate_state")
@RequiredArgsConstructor
public class CandidateStateController {

    private final CandidateStateService candidateStateService;

    @Operation(summary = "Add a new state to process ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "State created", content = @Content),
            @ApiResponse(responseCode = "409", description = "State already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CandidateStateResponseDto> saveCandidateState(@Valid @RequestBody CandidateStateRequestDto candidateStateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateStateService.addStateToProcess(candidateStateRequestDto));
    }

    @Operation(summary = "Get a candidate state by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "state found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateStateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "state not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CandidateStateResponseDto> getCandidateState(@PathVariable Long id) {
        CandidateStateResponseDto candidateStateResponseDto = candidateStateService.getCandidateStateById(id);
        return ResponseEntity.ok(candidateStateResponseDto);
    }

    @Operation(summary = "Get all the candidate state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All history candidate state returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateStateResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<CandidateStateResponseDto>> getAllCandidateState(){
        List<CandidateStateResponseDto> states = candidateStateService.getAllCandidateState();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Get all the candidate state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All history candidate state returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateStateResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/v2/")
    public PaginationResponseDto<CandidateStateResponseDto> getAllCandidateStates(
            @RequestParam(required = false) List<Status> status,
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size) {
        return candidateStateService.getAllCandidateStates(status, page, size);
    }

    @Operation(summary = "Get next valid states for a process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid next states returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StateResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Process not found", content = @Content)
    })
    @GetMapping("/process/{processId}/next-states")
    public ResponseEntity<NextValidStatesResponseDto> getNextValidStates(@PathVariable Long processId) {
        NextValidStatesResponseDto response = candidateStateService.getNextValidStates(processId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing candidate state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CandidateStateResponseDto> updateCandidateState(@Valid @PathVariable Long id, @Valid @RequestBody CandidateStateRequestUpdateDto candidateStateRequestUpdateDto){
        return candidateStateService.updateCandidateState(id, candidateStateRequestUpdateDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a candidate state by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "candidate state deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "candidate state not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidateState(@PathVariable Long id){
        candidateStateService.deleteCandidateState(id);
        return ResponseEntity.noContent().build();
    }
}