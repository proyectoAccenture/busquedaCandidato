package com.busquedaCandidato.candidato.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.service.OriginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/origin")
@RequiredArgsConstructor
public class OriginController {

     private final OriginService originService;

    @Operation(summary = "Get a origin by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Origin found",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OriginResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Origin not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OriginResponseDto> getByIdOrigin(@PathVariable Long id) {
        OriginResponseDto originResponseDto = originService.getOriginById(id);
        return ResponseEntity.ok(originResponseDto);
    }

    @Operation(summary = "Get all the origin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All origin returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OriginResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<OriginResponseDto>> getAllOrigin(){
         List<OriginResponseDto> origin = originService.getAllOrigin();
         return origin.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(origin);
    }

    @Operation(summary = "Add a new origin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Origin created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Origin already exists", content = @Content)
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
    public ResponseEntity<OriginResponseDto> updateOrigin(@Valid @PathVariable Long id, @Valid @RequestBody OriginRequestDto originRequestDto){
         OriginResponseDto updatedOrigin = originService.updateOrigin(id, originRequestDto);
         return ResponseEntity.ok(updatedOrigin);
    }

    @Operation(summary = "Delete a origin by its Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Origin deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Origin not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrigin(@PathVariable Long id){
        originService.deleteOrigin(id);
        return ResponseEntity.noContent().build();
    }
}
