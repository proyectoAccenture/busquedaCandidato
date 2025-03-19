package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.service.PostulationService;
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
@RequestMapping("/api/postulation")
@RequiredArgsConstructor
public class PostulationController {

    private final PostulationService postulationService;

    @Operation(summary = "Get a postulation by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulation found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostulationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Postulation not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostulationResponseDto> getPostulation(@PathVariable Long id){
        PostulationResponseDto getByIdPostulation = postulationService.getPostulation(id);
        return ResponseEntity.ok(getByIdPostulation);
    }

    @Operation(summary = "Get all the postulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All postulation returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostulationResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<PostulationResponseDto>> getAllPostulation(){
        List<PostulationResponseDto> states = postulationService.getAllPostulation();
        return states.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(states);
    }

    @Operation(summary = "Add a new postulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Postulation created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Postulation already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<PostulationResponseDto> savePostulation(@Valid @RequestBody PostulationRequestDto postulationRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(postulationService.savePostulation(postulationRequestDto));
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

    @Operation(summary = "Delete a postulation by their Number")
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
