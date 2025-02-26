package com.busquedaCandidato.candidato.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.service.JobProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/id")
@RequiredArgsConstructor
public class JobProfileController {
private final JobProfileService jobprofileService;

    @Operation(summary = "Get a jobprofile by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JobProfile found",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "JobProfile not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobProfileResponseDto> getState(@PathVariable Long id){
        return jobprofileService.getJobProfile(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the jobprofile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All jobprofile returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = JobProfileResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<JobProfileResponseDto>> getAllJobProfile(){
        List<JobProfileResponseDto> jobprofile = jobprofileService.getAllJobProfile();
        return jobprofile.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(jobprofile);
    }

    @Operation(summary = "Add a new jobprofile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "JobProfile created", content = @Content),
            @ApiResponse(responseCode = "409", description = "JobProfile already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<JobProfileResponseDto> saveJobProfile(@Valid @RequestBody JobProfileRequestDto jobProfileRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobprofileService.saveJobProfile(jobProfileRequestDto));
    }

    @Operation(summary = "Update an existing jobprofile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JobProfile updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "JobProfile not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<JobProfileResponseDto> updateState(@Valid @PathVariable Long id, @RequestBody JobProfileRequestDto jobprofileRequestDto){
        return jobprofileService.updateJobProfile(id, jobprofileRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a jobprofile by their Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JobProfile deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "JobProfile not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobProfile(@PathVariable Long id){
        return jobprofileService.deleteJobProfile(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
