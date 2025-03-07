package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<PostulationResponseDto> postulationOptional = postulationService.getPostulation(id);
        if (postulationOptional.isPresent()) {
            return ResponseEntity.ok(postulationOptional.get());
        } else {
            throw new EntityNotFoundException("Postulation not found");
        }
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
        try{
            List<PostulationResponseDto> postulation = postulationService.getAllPostulation();
            return ResponseEntity.ok(postulation);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
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
    public ResponseEntity<PostulationResponseDto> updatePostulation(@Valid @PathVariable Long id, @RequestBody PostulationRequestDto postulationRequestDto){
        Optional<PostulationResponseDto> postulationOptional = postulationService.updatePostulation(id, postulationRequestDto);
        if (postulationOptional.isPresent()) {
            return ResponseEntity.ok(postulationOptional.get());
        } else {
            throw new EntityNotFoundException("Postulation not found");
        }
    }

    @Operation(summary = "Delete a postulation by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulation deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Postulation not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostulation(@PathVariable Long id){
        boolean isDeleted = postulationService.deletePostulation(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Postulation not found");
        }
    }
}
