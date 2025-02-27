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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones relacionadas con los perfiles de trabajo.
 */
@RestController
@RequestMapping("/api/jobprofile")
@RequiredArgsConstructor
public class JobProfileController {

    private final JobProfileService jobProfileService;


    /**
     * Obtiene un perfil de trabajo por su ID.
     *
     * @param id El ID del perfil de trabajo.
     * @return ResponseEntity con el JobProfileResponseDto se encuentra, de lo contrario 404.
     */
    @Operation(summary = "Get a job profile by ist ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = JobProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Job profile not found",content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobProfileResponseDto> getByIdJobProfile(@PathVariable Long id){
        return jobProfileService.getJobProfile(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    /**
     * Obtiene todos los perfiles de trabajo.
     *
     * @return ResponseEntity con una lista de JobProfileResponseDto o 204 si no hay perfiles.
     */
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





    /**
     * Añade un nuevo perfil de trabajo.
     *
     * @param jobProfileRequestDto El DTO que representa la solicitud de creación de un perfil de trabajo.
     * @return ResponseEntity con el JobProfileResponseDto del perfil de trabajo creado.
     */
    @Operation(summary = "Add a new jo profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job profile created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Job profile already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<JobProfileResponseDto> saveJobProfile(@Valid @RequestBody JobProfileRequestDto jobProfileRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobProfileService.saveJobProfile(jobProfileRequestDto));
    }



    /**
     * Actualiza un perfil de trabajo existente.
     *
     * @param id El ID del perfil de trabajo.
     * @param jobProfileRequestDto El DTO que representa la solicitud de actualización de un perfil de trabajo.
     * @return ResponseEntity con el JobProfileResponseDto actualizado.
     */
    @Operation(summary = "Update an existing job profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job profile not found", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<JobProfileResponseDto> updateJobProfile(@Valid @PathVariable Long id, @RequestBody JobProfileRequestDto jobProfileRequestDto){
        return jobProfileService.updateJobProfile(id, jobProfileRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }






    /**
     * Elimina un perfil de trabajo por su ID.
     *
     * @param id El ID del perfil de trabajo.
     * @return ResponseEntity con estado 204 si el perfil de trabajo fue eliminado, de lo contrario 404.
     */
    @Operation(summary = "Delete a job profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job profile deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job profile not found", content = @Content)
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobProfile(@PathVariable Long id){
        return jobProfileService.deleteJobProfile(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }



}//Fin de la clase JobProfileController
