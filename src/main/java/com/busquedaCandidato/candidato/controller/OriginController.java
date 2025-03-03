package com.busquedaCandidato.candidato.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityNotFoundException;
import com.busquedaCandidato.candidato.service.OriginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/origin")
@RequiredArgsConstructor
public class OriginController {

     private final OriginService originService;

    @Operation(summary = "Get a origin by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Origin found",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OriginResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Origin not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OriginResponseDto> getOrigin(@PathVariable Long id){
        Optional<OriginResponseDto> stateOptional = originService.getOrigin(id);
                if (stateOptional.isPresent()) {
                        return ResponseEntity.ok(stateOptional.get());
                } else {
                        throw new EntityNotFoundException("State not found");
                }
    }

     @Operation(summary = "Get all the origin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All origin returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OriginResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)         
    })
    @GetMapping("/")
    public ResponseEntity<List<OriginResponseDto>> getAllOrigin(){
        try{
                List<OriginResponseDto> origin = originService.getAllOrigin();
                return ResponseEntity.ok(origin);
            } catch (Exception e){
                return ResponseEntity.internalServerError().build();
            }
    }

     @Operation(summary = "Add a new origin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Origin created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Origin already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "Origin invalid request", content = @Content)
        })
    
    @PostMapping("/")
    public ResponseEntity<OriginResponseDto> saveOrigin(@Valid @RequestBody OriginRequestDto originRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(originService.saveOrigin(originRequestDto));
    }

     @Operation(summary = "Update an existing origin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Origin updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Origin not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OriginResponseDto> updateOrigin(@Valid @PathVariable Long id, @RequestBody OriginRequestDto originRequestDto){
        Optional<OriginResponseDto> stateOptional = originService.updateOrigin(id, originRequestDto);
                if (stateOptional.isPresent()) {
                        return ResponseEntity.ok(stateOptional.get());
                } else {
                        throw new EntityNotFoundException("State not found");
                }
    }

    @Operation(summary = "Delete a origin by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Origin deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Origin not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrigin(@PathVariable Long id){
        boolean isDeleted = originService.deleteOrigin(id);  
                if (isDeleted) {
                    return ResponseEntity.noContent().build();  
                } else {
                    throw new EntityNotFoundException("State not found");
                }
    }


}
