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


@RestController
@RequestMapping("/api/roleid")
@RequiredArgsConstructor
public class RoleIDController {

    private final RoleIDService roleIDService;

    @Operation(summary = "Get a Role ID by ist ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleIDResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })

    @GetMapping("/{id}")
    public ResponseEntity<RoleIDResponseDto> getByIdRoleID(@PathVariable Long id){
        Optional<RoleIDResponseDto> rolIdOptional = roleIDService.getRolID(id);
        if (rolIdOptional.isPresent()) {
            return ResponseEntity.ok(rolIdOptional.get());
        } else {
            throw new EntityNotFoundException("RolId not found");
        }
    }

    @Operation(summary = "Get all the rolID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Role ID returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoleIDResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)

    })

    @GetMapping("/")
    public ResponseEntity<List<RoleIDResponseDto>> getAllRoleID(){
        try{
            List<RoleIDResponseDto> rolId = roleIDService.getAllRolID();
            return ResponseEntity.ok(rolId);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Add a new role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol id created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Rol id already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RoleIDResponseDto> saveRoleID(@Valid @RequestBody RoleIDRequestDto rolIDRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(roleIDService.saveRolID(rolIDRequestDto));
    }

    @Operation(summary = "Update an existing rol id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<RoleIDResponseDto> updateRoleID(@Valid @PathVariable Long id, @RequestBody RoleIDRequestDto rolIDRequestDto){
        Optional<RoleIDResponseDto> rolIdOptional = roleIDService.updateRolID(id, rolIDRequestDto);
        if (rolIdOptional.isPresent()) {
            return ResponseEntity.ok(rolIdOptional.get());
        } else {
            throw new EntityNotFoundException("RoleId not found");
        }
    }

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
            throw new EntityNotFoundException("Roleid not found");
        }
    }

}
