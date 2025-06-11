package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.RoleRequestDto;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get a role with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id){
        RoleResponseDto roleResponseDto = roleService.getRole(id);
        return ResponseEntity.ok(roleResponseDto);
    }

    @Operation(summary = "Get all the roles with pagination and filter by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All role returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoleResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<PaginationResponseDto<RoleResponseDto>> getAllPostulations(
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size) {

        return ResponseEntity.ok(roleService.getAllRoles(statuses, page, size));
    }

    @Operation(summary = "Search roles by all fields with pagination and filter by status",
            description = "Fetches roles that match the search keyword in any of the RoleEntity fields, with optional " +
                    "filtering by status and pagination of results.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoleResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No roles found", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<PaginationResponseDto<RoleResponseDto>> search(
            @RequestParam @NotBlank String query,
            @RequestParam(name = "status", required = false) List<Status> statuses,
            @RequestParam(defaultValue = "0") @Min(0)int page,
            @RequestParam(defaultValue = "10")@Min(1) int size){
        return ResponseEntity.ok(roleService.searchRoles(query, statuses, page, size));
    }

    @Operation(summary = "Add a new role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Role already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RoleResponseDto> saveRole(@Valid @RequestBody RoleRequestDto roleRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.saveRole(roleRequestDto));
    }

    @Operation(summary = "Update an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@Valid @PathVariable Long id, @Valid @RequestBody RoleRequestDto roleRequestDto){
        return roleService.updateRole(id, roleRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a role with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
