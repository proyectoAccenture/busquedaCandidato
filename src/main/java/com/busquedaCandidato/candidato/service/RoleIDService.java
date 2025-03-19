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

@Service
@AllArgsConstructor
public class RoleIDService {

    private final IRoleIDRepository roleIDRepository;
    private final IMapperRoleIDResponse mapperRolIDResponse;
    private final IMapperRoleIDRequest mapperRolIDRequest;

    public Optional<RoleIDResponseDto> getRolID(Long id) {
        return roleIDRepository.findById(id)
                .map(mapperRolIDResponse::toDto);
    }

    public List<RoleIDResponseDto> getAllRolID() {
        return roleIDRepository.findAll().stream()
                .map(mapperRolIDResponse::toDto)
                .collect(Collectors.toList());
    }

    public RoleIDResponseDto saveRolID(RoleIDRequestDto rolIDRequestDto) {
        if (roleIDRepository.existsByName(rolIDRequestDto.getName())) {
            throw new EntityAlreadyExistsException();
        }
        RoleIDEntity roleIDEntity = mapperRolIDRequest.toEntity(rolIDRequestDto);
        RoleIDEntity roleIDEntitySave = roleIDRepository.save(roleIDEntity);
        return mapperRolIDResponse.toDto(roleIDEntitySave);

    }

    public Optional<RoleIDResponseDto> updateRolID(Long id, RoleIDRequestDto rolIDRequestDto) {
        return roleIDRepository.findById(id)
                .map(existing -> {
                    existing.setName(rolIDRequestDto.getName());
                    return mapperRolIDResponse.toDto(roleIDRepository.save(existing));
                });
    }

    public boolean deleteRolID(Long id) {
        if (roleIDRepository.existsById(id)) {
            roleIDRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
