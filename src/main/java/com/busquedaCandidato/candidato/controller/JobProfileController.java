package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityNotFoundException;
import com.busquedaCandidato.candidato.service.JobProfileService;
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
@RequestMapping("/api/jobprofile")
@RequiredArgsConstructor
public class JobProfileController {

    private final JobProfileService jobProfileService;


    @Operation(summary = "Get a job profile by ist ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = JobProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Job profile not found",content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobProfileResponseDto> getByIdJobProfile(@PathVariable Long id){
        Optional<JobProfileResponseDto> JobProfileOptional = jobProfileService.getJobProfile(id);
        if (JobProfileOptional.isPresent()) {
            return ResponseEntity.ok(JobProfileOptional.get());
        } else {
            throw new EntityNotFoundException("JobProfile not found");
        }
    }

    @Operation(summary = "Get all  job profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All job profiles returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = JobProfileResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })

    @GetMapping("/")
    public ResponseEntity<List<JobProfileResponseDto>> getAllJobProfile(){
        try{
            List<JobProfileResponseDto> JobProfile = jobProfileService.getAllJobProfile();
            return ResponseEntity.ok(JobProfile);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Add a new job profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job profile created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Job profile already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<JobProfileResponseDto> saveJobProfile(@Valid @RequestBody JobProfileRequestDto jobProfileRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobProfileService.saveJobProfile(jobProfileRequestDto));
    }

    @Operation(summary = "Update an existing job profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job profile not found", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<JobProfileResponseDto> updateJobProfile(@Valid @PathVariable Long id, @RequestBody JobProfileRequestDto jobProfileRequestDto){
        Optional<JobProfileResponseDto> jobProfileOptional = jobProfileService.updateJobProfile(id, jobProfileRequestDto);
        if (jobProfileOptional.isPresent()) {
            return ResponseEntity.ok(jobProfileOptional.get());
        } else {
            throw new EntityNotFoundException("JobProfile not found");
        }
    }

    @Operation(summary = "Delete a job profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job profile not found", content = @Content)
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobProfile(@PathVariable Long id){
        boolean isDeleted = jobProfileService.deleteJobProfile(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("JobProfile not found");
        }
    }



}
