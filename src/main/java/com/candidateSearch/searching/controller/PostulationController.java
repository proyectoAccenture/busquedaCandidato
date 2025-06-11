package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.PostulationRequestDto;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.dto.response.PostulationFullResponseDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.service.PostulationService;
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
@RequestMapping("/api/postulation")
@RequiredArgsConstructor
public class PostulationController {

    private final PostulationService postulationService;

    @Operation(summary = "Get a postulation by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulation found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostulationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Postulation not found", content = @Content)
    })
    @GetMapping("/full/{id}")
    public ResponseEntity<PostulationFullResponseDto> getPostulationFull(@PathVariable Long id){
        PostulationFullResponseDto postulation = postulationService.getPostulationFullById(id);
        return ResponseEntity.ok(postulation);
    }

    @Operation(summary = "Get a postulation by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulation found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostulationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Postulation not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostulationResponseDto> getByIdPostulation(@PathVariable Long id){
        PostulationResponseDto getByIdPostulation = postulationService.getPostulationById(id);
        return ResponseEntity.ok(getByIdPostulation);
    }

    @Operation(summary = "Search postulations by candidate name or last name")
    @ApiResponse(responseCode = "200", description = "Postulations found",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PostulationResponseDto.class))))
    @ApiResponse(responseCode = "400", description = "Invalid query", content = @Content)
    @ApiResponse(responseCode = "404", description = "No postulations found", content = @Content)
    @GetMapping("/search/fullName")
    public List<PostulationResponseDto> getSearchPostulationsByCandidate(@RequestParam @NotBlank String query) {
        return postulationService.getSearchPostulationsByCandidateFullName(query);
    }

    @Operation(summary = "Search postulations by all fields with pagination, filter by status, and sorting",
            description = "Fetches postulations that match the search keyword in any of the PostulationEntity fields, with optional " +
                    "filtering by status and pagination of results.")
    @ApiResponse(responseCode = "200", description = "Postulations found",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PostulationResponseDto.class))))
    @ApiResponse(responseCode = "400", description = "Invalid query", content = @Content)
    @ApiResponse(responseCode = "404", description = "No postulations found", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<PaginationResponseDto<PostulationResponseDto>> search(
            @RequestParam @NotBlank String query,
            @RequestParam(name = "status", required = false) List<Status> statuses,
            @RequestParam(defaultValue = "0") @Min(0)int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction){
        return ResponseEntity.ok(postulationService.searchByCandidateNameLastNameAndRole(query, statuses, page, size,sortBy,direction));
    }

    @Operation(summary = "Get all the postulation with pagination, filter by status, and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All postulation returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostulationResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<PaginationResponseDto<PostulationResponseDto>> getAllPostulations(
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "datePresentation") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(postulationService.getAllPostulation(statuses, page, size,sortBy,direction));
    }

    @Operation(summary = "Add a new postulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Postulation created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Active postulation already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<PostulationResponseDto> savePostulation(@Valid @RequestBody PostulationRequestDto postulationRequestDto) {
            PostulationResponseDto response = postulationService.savePostulation(postulationRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing postulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulation updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Postulation not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostulationResponseDto> updatePostulation(@Valid @PathVariable Long id, @Valid @RequestBody PostulationRequestDto postulationRequestDto){
        return postulationService.updatePostulation(id, postulationRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a postulation by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulation deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Postulation not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostulation(@PathVariable Long id){
        postulationService.deletePostulation(id);
        return ResponseEntity.noContent().build();
    }
}
