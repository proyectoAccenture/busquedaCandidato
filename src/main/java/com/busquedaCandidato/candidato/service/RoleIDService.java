package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperRoleIDRequest;
import com.busquedaCandidato.candidato.mapper.IMapperRoleIDResponse;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleIDService {

    private final IRoleIDRepository roleIDRepository;
    private final IMapperRoleIDResponse mapperRolIDResponse;
    private final IMapperRoleIDRequest mapperRolIDRequest;

    public RoleIDResponseDto getRolID(Long id) {
        return roleIDRepository.findById(id)
                .map(mapperRolIDResponse::RolIdToRolIdResponse)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<RoleIDResponseDto> getAllRolID() {
        return roleIDRepository.findAll().stream()
                .map(mapperRolIDResponse::RolIdToRolIdResponse)
                .collect(Collectors.toList());
    }

    public RoleIDResponseDto saveRolID(RoleIDRequestDto rolIDRequestDto) {
        if (roleIDRepository.existsByName(rolIDRequestDto.getName())) {
            throw new EntityAlreadyExistsException();
        }

        RoleIDEntity roleIDEntity = mapperRolIDRequest.RolIDRequestToStatus(rolIDRequestDto);
        RoleIDEntity roleIDEntitySave = roleIDRepository.save(roleIDEntity);

        return mapperRolIDResponse.RolIdToRolIdResponse(roleIDEntitySave);
    }

    public RoleIDResponseDto updateRolID(Long id, RoleIDRequestDto rolIDRequestDto) {
        RoleIDEntity existingRolId = roleIDRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingRolId.setName(rolIDRequestDto.getName());
        RoleIDEntity updatedRolId = roleIDRepository.save(existingRolId);

        return mapperRolIDResponse.RolIdToRolIdResponse(updatedRolId);
    }

    public void deleteRolID(Long id) {
        RoleIDEntity existingRolId = roleIDRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        roleIDRepository.delete(existingRolId);
    }

}
