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


import com.busquedaCandidato.candidato.dto.request.RolIDRequestDto;
import com.busquedaCandidato.candidato.dto.response.RolIDResponseDto;
import com.busquedaCandidato.candidato.service.RoleIDService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


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
       schema = @Schema(implementation = RolIDResponseDto.class))),
       @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolIDResponseDto> getByIdRoleID(@PathVariable Long id){
        return roleIDService.getRolID(id)
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Obtiene todos los Role ID.
     *
     * @return ResponseEntity con una lista de RoleIDResponseDto o 204 si no hay role id.
     */
    @Operation(summary = "Get all Role ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All Role ID returned",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RolIDResponseDto.class)))),
    @ApiResponse(responseCode = "404", description = "No data found", content = @Content)

    })

    @GetMapping("/")
    public ResponseEntity<List<RolIDResponseDto>> getAllRoleID(){
        List<RolIDResponseDto> roleid = roleIDService.getAllJRoleID();
        return roleid.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(roleid);
    }

    /**
     * Añade un nuevo Role ID.
     *
     * @param roleIDrequestDto El DTO que representa la solicitud de creación de un Role ID.
     * @return ResponseEntity con el RoleIDResponseDto del role id creado.
     */
    @Operation(summary = "Add a new role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol id created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Rol id already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RolIDResponseDto> saveRoleID(@Valid @RequestBody RolIDRequestDto rolIDRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(roleIDService.saveRolID(rolIDRequestDto));
    }

    /**
     * Actualiza un Role ID existente.
     *
     * @param id El ID del Role ID.
     * @param roleIDrequestDto El DTO que representa la solicitud de actualización de un rol id.
     * @return ResponseEntity con el RoleIDResponseDto actualizado.
     */
    @Operation(summary = "Update an existing rol id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<RolIDResponseDto> updateRoleID(@Valid @PathVariable Long id, @RequestBody RolIDRequestDto rolIDRequestDto){
        return roleIDService.updateRolID(id, rolIDRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
        return roleIDService.deleteRolID(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}//Fin de la clase RoleIDController
