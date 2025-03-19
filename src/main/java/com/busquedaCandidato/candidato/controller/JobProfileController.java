package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
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
@RequestMapping("/api/job_profile")
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
        JobProfileResponseDto jobProfileResponseDto = jobProfileService.getJobProfile(id);
        return ResponseEntity.ok(jobProfileResponseDto);
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
        List<JobProfileResponseDto> job = jobProfileService.getAllJobProfile();
        return job.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(job);
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
        JobProfileResponseDto updatedJobProfile = jobProfileService.updateJobProfile(id, jobProfileRequestDto);
        return ResponseEntity.ok(updatedJobProfile);
    }

    @Operation(summary = "Delete a job profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job profile not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobProfile(@PathVariable Long id){
        jobProfileService.deleteJobProfile(id);
        return ResponseEntity.noContent().build();
    }
}
