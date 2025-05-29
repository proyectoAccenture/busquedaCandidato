package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.ProcessRequestDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.dto.response.ProcessResponseDto;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.service.ProcessService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("api/process")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @Operation(summary = "Get a process by their role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/role/{idRole}")
    public ResponseEntity<List<ProcessResponseDto>> getCandidateByRole(@PathVariable String idRole){
        List<ProcessResponseDto> processCandidateList = processService.getProcessOfCandidateByRole(idRole);
        return ResponseEntity.ok(processCandidateList);
    }

    @Operation(summary = "Get a process by its number of the candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProcessEntity found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProcessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "ProcessEntity not found", content = @Content)
    })
    @GetMapping("/candidate/{id}")
    public ResponseEntity<ProcessResponseDto> getProcessByPostulationId(@PathVariable Long id){
        ProcessResponseDto processResponseDto  = processService.getProcessByIdCandidate(id);
        return ResponseEntity.ok(processResponseDto );
    }

    @Operation(summary = "Get a process by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProcessEntity found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProcessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "ProcessEntity not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProcessResponseDto> getByIdProcess(@PathVariable Long id){
        ProcessResponseDto processResponseDto  = processService.getByIdProcess(id);
        return ResponseEntity.ok(processResponseDto );
    }

    @Operation(summary = "Get the processes by the postulation ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProcessResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Postulation ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/by-postulation/{postulationId}")
    public List<ProcessResponseDto> getProcessesByPostulationId(
            @PathVariable @Valid @Min(1) Long postulationId) {
        return processService.getProcessByPostulationId(postulationId);
    }

    @Operation(summary = "Search processes by candidate name or last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processes found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProcessResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/search/fullName")
    public List<ProcessResponseDto> searchProcessesByCandidate(@RequestParam String query) {
        return processService.getSearchProcessesByCandidateFullName(query);
    }

    @Operation(summary = "Search processes by candidate name or last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processes found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProcessResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/v2/search/fullName")
    public ResponseEntity<PaginationResponseDto<ProcessResponseDto>> search(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "0") @Min(0)int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(name = "status", required = false) List<Status> statuses){
        return ResponseEntity.ok(processService.getSearchProcessesByCandidateFullNameV2(query, page, size,statuses));
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

    @Operation(summary = "Get all the process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All process returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProcessResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/v2/")
    public ResponseEntity<PaginationResponseDto<ProcessResponseDto>> getAllProcessV2(
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size) {
        return  ResponseEntity.ok(processService.getAllProcessV2(statuses,page,size));

    }
        @Operation(summary = "Add a new process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ProcessEntity created", content = @Content),
            @ApiResponse(responseCode = "409", description = "ProcessEntity already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<ProcessResponseDto> saveCandidateStatusHistory(@Valid @RequestBody ProcessRequestDto processRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(processService.saveProcess(processRequestDto));
    }

    @Operation(summary = "Update an existing process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProcessResponseDto> updateProcess(@Valid @PathVariable Long id, @Valid @RequestBody ProcessRequestDto processRequestDto){
        return processService.updateProcess(id, processRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a process by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "process not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Long id){
        processService.deleteProcess(id);
        return ResponseEntity.noContent().build();
    }
}

