package com.busquedaCandidato.candidato.controller;


import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityNotFoundException;
import com.busquedaCandidato.candidato.service.RoleIDService;
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

/**
 * Controlador REST para manejar las operaciones relacionadas con los role ID.
 */
@RestController
@RequestMapping("/api/roleid")
@RequiredArgsConstructor
public class RoleIDController {

    private final RoleIDService roleIDService;

    /**
     * Obtiene un Role ID por su ID.
     *
     * @param id El ID del Role ID.
     * @return ResponseEntity con el RolIDResponseDto se encuentra, de lo contrario 404.
     */
    @Operation(summary = "Get a Role ID by ist ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleIDResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })

    @GetMapping("/{id}")
    public ResponseEntity<RoleIDResponseDto> getByIdRoleID(@PathVariable Long id){
        Optional<RoleIDResponseDto> stateOptional = roleIDService.getRolID(id);
                if (stateOptional.isPresent()) {
                        return ResponseEntity.ok(stateOptional.get());
                } else {
                        throw new EntityNotFoundException("State not found");
                }
    }

    /**
     * Obtiene todos los Role ID.
     *
     * @return ResponseEntity con una lista de RoleIDResponseDto o 204 si no hay role id.
     */
    @Operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Role ID returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoleIDResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)

    })

    @GetMapping("/")
    public ResponseEntity<List<RoleIDResponseDto>> getAllRoleID(){
        try{
                List<RoleIDResponseDto> roleID = roleIDService.getAllRoleID();
                return ResponseEntity.ok(roleID);
            } catch (Exception e){
                return ResponseEntity.internalServerError().build();
            }
    }

    /**
     * Añade un nuevo Role ID.
     *
     * @param rolIDRequestDto El DTO que representa la solicitud de creación de un Role ID.
     * @return ResponseEntity con el RoleIDResponseDto del role id creado.
     */
    @Operation(summary = "Add a new role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol id created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Rol id already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "Rol id invalid request", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RoleIDResponseDto> saveRoleID(@Valid @RequestBody RoleIDRequestDto rolIDRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(roleIDService.saveRolID(rolIDRequestDto));
    }

    /**
     * Actualiza un Role ID existente.
     *
     * @param id El ID del Role ID.
     * @param rolIDRequestDto El DTO que representa la solicitud de actualización de un rol id.
     * @return ResponseEntity con el RoleIDResponseDto actualizado.
     */
    @Operation(summary = "Update an existing rol id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<RoleIDResponseDto> updateRoleID(@Valid @PathVariable Long id, @RequestBody RoleIDRequestDto rolIDRequestDto){
        Optional<RoleIDResponseDto> stateOptional = roleIDService.updateRolID(id, rolIDRequestDto);
                if (stateOptional.isPresent()) {
                        return ResponseEntity.ok(stateOptional.get());
                } else {
                        throw new EntityNotFoundException("State not found");
                }
    }

    /**
     * Elimina un Role ID por su ID.
     *
     * @param id El ID del Role ID.
     * @return ResponseEntity con estado 204 si el role id fue eliminado, de lo contrario 404.
     */
    @Operation(summary = "Delete a role id by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role id deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role id not found", content = @Content)
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleID(@PathVariable Long id){
        boolean isDeleted = roleIDService.deleteRolID(id);  
                if (isDeleted) {
                    return ResponseEntity.noContent().build();  
                } else {
                    throw new EntityNotFoundException("State not found");
                }
    }

}//Fin de la clase RoleIDController
