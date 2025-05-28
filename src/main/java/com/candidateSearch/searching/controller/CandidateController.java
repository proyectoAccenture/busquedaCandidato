package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.request.PostulationRequestDto;
import com.candidateSearch.searching.dto.response.CandidateWithPaginationResponseDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.CandidateResumeResponseDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.service.CandidateResumeService;
import com.candidateSearch.searching.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final CandidateResumeService candidateResumeService;

    @Operation(summary = "Veto candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vetoed candidate", content = @Content),
            @ApiResponse(responseCode = "409", description = "Candidate is already vetoed", content = @Content)
    })
    @PatchMapping("/{card}/")
    public ResponseEntity<CandidateResponseDto> vetoCandidate(@PathVariable String card) {
        CandidateResponseDto response = candidateService.vetaCandidate(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all the vetoed candidates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All vetoed candidates",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostulationResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/veto/")
    public ResponseEntity<List<CandidateResponseDto>> getAllCandidateVetoed(){
        List<CandidateResponseDto> candidates = candidateService.getAllCandidateVeto();
        return candidates.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(candidates);
    }

    @Operation(summary = "Get a candidate by its role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @GetMapping("/role/{nameRole}")
    public ResponseEntity<List<CandidateResponseDto>> getCandidateByRole(@PathVariable String nameRole){
        List<CandidateResponseDto> candidates = candidateService.getCandidateByRole(nameRole);
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
    public ResponseEntity<CandidateResponseDto> getCandidateById(@PathVariable Long id){
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
    public ResponseEntity<CandidateWithPaginationResponseDto> getSearchCandidates(
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
    @GetMapping("/search-fullName/")
    public ResponseEntity<CandidateWithPaginationResponseDto> getSearchCandidatesFullName(@RequestParam("fullName") @NotBlank String fullName, @RequestParam("status") @NotBlank String status) {
        CandidateWithPaginationResponseDto candidateWithPaginationResponseDto = candidateService.getSearchCandidatesFullName(fullName, status);
        if (candidateWithPaginationResponseDto.toString().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidateWithPaginationResponseDto);
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
        List<CandidateResponseDto> candidates = candidateService.getAllCandidate();
        return candidates.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(candidates);
    }

    @Operation(summary = "Add a new candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidate created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Candidate already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CandidateResponseDto> saveCandidate(
            @RequestBody @Valid CandidateRequestDto candidateRequestDto) {

        CandidateResponseDto savedCandidate = candidateService.saveCandidate(candidateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }

    @Operation(summary = "Upload a candidate's resume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resume uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Candidate not found", content = @Content)
    })
    @PostMapping(value = "/{candidateId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadResume(
            @Parameter(description = "candidate ID", required = true)
            @PathVariable Long candidateId,
            @Parameter(description = "PDF file of the resume",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file) {
        candidateResumeService.uploadResume(candidateId, file);

        return ResponseEntity.ok("Resume uploaded successfully");
    }

    @Operation(summary = "Create a new candidate with a resume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidate created with resume",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Candidate already exists", content = @Content)
    })
    @PostMapping(value = "/with-resume",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CandidateResponseDto> saveCandidateWithResume(
            @Parameter(description = "Candidate details", required = true)
            @RequestPart("candidate") @Valid CandidateRequestDto candidateRequestDto,
            @Parameter(description = "Resume PDF (optional)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "file", required = false) MultipartFile file) {

        CandidateResponseDto savedCandidate = candidateResumeService.saveCandidateWithResume(candidateRequestDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }

    @Operation(summary = "Download a candidate's resume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found resume",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Resume not found", content = @Content)
    })
    @GetMapping("/{candidateId}/download")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long candidateId) {
        CandidateResumeResponseDto resume = candidateResumeService.getResume(candidateId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(resume.getResumeContentType()));
        headers.setContentDispositionFormData("attachment", resume.getResumeFileName());

        return new ResponseEntity<>(resume.getResumePdf(), headers, HttpStatus.OK);
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
