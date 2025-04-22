package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.RoleRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleResponseDto;
import com.busquedaCandidato.candidato.service.RoleService;
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
@RequestMapping("/api/role_id")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get a Role ID by ist ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getByIdRoleID(@PathVariable Long id){
        RoleResponseDto roleResponseDto = roleService.getRolID(id);
        return ResponseEntity.ok(roleResponseDto);
    }

    @Operation(summary = "Get all the rolID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Role ID returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoleResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<RoleResponseDto>> getAllRoleID(){
        List<RoleResponseDto> role_id = roleService.getAllRolID();
        return role_id.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(role_id);
    }

    @Operation(summary = "Add a new role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol id created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Rol id already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RoleResponseDto> saveRoleID(@Valid @RequestBody RoleRequestDto rolIDRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.saveRolID(rolIDRequestDto));
    }

    @Operation(summary = "Update an existing rol id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role ID updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role ID not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRoleID(@Valid @PathVariable Long id, @Valid @RequestBody RoleRequestDto rolIDRequestDto){
        RoleResponseDto updatedRolId = roleService.updateRolID(id, rolIDRequestDto);
        return ResponseEntity.ok(updatedRolId);
    }

    @Operation(summary = "Delete a role id by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role id deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role id not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleID(@PathVariable Long id){
        roleService.deleteRolID(id);
        return ResponseEntity.noContent().build();
    }
}
