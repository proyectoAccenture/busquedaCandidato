package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperRoleIDRequest;
import com.busquedaCandidato.candidato.mapper.IMapperRoleIDResponse;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para manejar las operaciones relacionadas con el role ID.
 */
@Service
@AllArgsConstructor
public class RoleIDService {

    private final IRoleIDRepository roleIDRepository;
    private final IMapperRoleIDResponse mapperRolIDResponse;
    private final IMapperRoleIDRequest mapperRolIDRequest;

    /**
     * Obtiene un Role ID por su ID.
     *
     * @param id El ID del Role ID.
     * @return Un Optional que contiene el RolIDResponseDto si se encuentra, de lo
     *         contrario vacío.
     */
    public Optional<RoleIDResponseDto> getRolID(Long id) {
        return roleIDRepository.findById(id)
                .map(mapperRolIDResponse::RolIDToStatusResponse);
    }

    /**
     * Obtiene todos los perfiles de trabajo.
     *
     * @return Una lista de RolIDResponseDto.
     */
    public List<RoleIDResponseDto> getAllJRoleID() {
        return roleIDRepository.findAll().stream()
                .map(mapperRolIDResponse::RolIDToStatusResponse)
                .collect(Collectors.toList());
    }

    /**
     * Guarda un nuevo Role ID.
     *
     * @param rolIDRequestDto El DTO que representa la solicitud de creación de un Role ID.
     * @return El RolIDResponseDto del role ID guardado.
     * @throws EntityAlreadyExistsException si ya existe un role ID con el mismo nombre.
     */
    public RoleIDResponseDto saveRolID(RoleIDRequestDto rolIDRequestDto) {
        if (roleIDRepository.existsByName(rolIDRequestDto.getName())) {
            throw new EntityAlreadyExistsException();
        }
        RoleIDEntity roleIDEntity = mapperRolIDRequest.RolIDRequestToStatus(rolIDRequestDto);
        RoleIDEntity roleIDEntitySave = roleIDRepository.save(roleIDEntity);
        return mapperRolIDResponse.RolIDToStatusResponse(roleIDEntitySave);

    }

    /**
     * Actualiza un Role ID existente.
     *
     * @param id              El ID del Role ID.
     * @param rolIDRequestDto El DTO que representa la solicitud de actualización de un Rol ID.
     * @return Un Optional que contiene el RolIDResponseDto actualizado si se encuentra, de lo contrario vacío.
     */
    public Optional<RoleIDResponseDto> updateRolID(Long id, RoleIDRequestDto rolIDRequestDto) {
        return roleIDRepository.findById(id)
                .map(existing -> {
                    existing.setName(rolIDRequestDto.getName());
                    return mapperRolIDResponse.RolIDToStatusResponse(roleIDRepository.save(existing));
                });
    }

    /**
     * Elimina un Role ID por su ID.
     *
     * @param id El ID del Role ID.
     * @return true si el Role ID fue eliminado, de lo contrario false.
     */
    public boolean deleteRolID(Long id) {
        if (roleIDRepository.existsById(id)) {
            roleIDRepository.deleteById(id);
            return true;
        }
        return false;
    }

}// Fin de la clase RoleIDService.
