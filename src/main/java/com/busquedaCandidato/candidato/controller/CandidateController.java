package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponse;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @Operation(summary = "Get a candidate by its role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/role/{idRole}")
    public ResponseEntity<List<CandidateResponseDto>> geCandidateByRole(@PathVariable String idRole){
        List<CandidateResponseDto> candidates = candidateService.getCandidateByRole(idRole);
        return ResponseEntity.ok(candidates);
    }

    @Operation(summary = "Get a candidate by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<CandidateResponseDto> getByIdCandidate(@PathVariable Long id){
        CandidateResponseDto candidate = candidateService.getByIdCandidate(id);
        return ResponseEntity.ok(candidate);
    }

    @Operation(summary = "Get a candidate by any field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<CandidateResponse> getSearchCandidates(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "0") @Min(0)int page,
            @RequestParam(defaultValue = "10")@Min(1)  int size) {
        return ResponseEntity.ok(candidateService.getSearchCandidates(query, page, size));
    }

    @Operation(summary = "Get candidates by full name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidates found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid query", content = @Content),
            @ApiResponse(responseCode = "404", description = "No candidates found", content = @Content)
    })
    @GetMapping("/search-fullName/{fullName}")
    public ResponseEntity<CandidateResponse> getSearchCandidatesFullName(@PathVariable @NotBlank String fullName) {
        CandidateResponse candidateResponse = candidateService.getSearchCandidatesFullName(fullName);
        if (candidateResponse.toString().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateResponse);
    }

    @Operation(summary = "Get all the candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All candidate returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CandidateResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<CandidateResponseDto>> getAllCandidate(){
        List<CandidateResponseDto> states = candidateService.getAllCandidate();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidate created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Candidate already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CandidateResponseDto> saveCandidate(@Valid @RequestBody CandidateRequestDto candidateRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.saveCandidate(candidateRequestDto));
    }

    @Operation(summary = "Update an existing candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "State not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDto> updateCandidate(@Valid @PathVariable Long id,@Valid @RequestBody CandidateRequestDto candidateRequestDto){
        return candidateService.updateCandidate(id, candidateRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a candidate by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id){
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}
