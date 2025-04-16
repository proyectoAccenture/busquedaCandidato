package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperRoleIDRequest;
import com.busquedaCandidato.candidato.mapper.IMapperRoleIDResponse;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleIDService {

    private final IRoleIDRepository roleIDRepository;
    private final IVacancyCompanyRepository vacancyCompanyRepository;
    private final IMapperRoleIDResponse mapperRolIDResponse;
    private final IMapperRoleIDRequest mapperRolIDRequest;
    private final IPostulationRepository postulationRepository;

    public RoleIDResponseDto getRolID(Long id) {
        return roleIDRepository.findById(id)
                .map(mapperRolIDResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<RoleIDResponseDto> getAllRolID() {
        return roleIDRepository.findAll().stream()
                .map(mapperRolIDResponse::toDto)
                .collect(Collectors.toList());
    }

    public RoleIDResponseDto saveRolID(RoleIDRequestDto rolIDRequestDto) {

        VacancyCompanyEntity vacancyCompanyEntity = null;

        if (rolIDRequestDto.getVacancyCompanyId() != null) {
            Optional<VacancyCompanyEntity> optionalVacancyCompanyEntity = vacancyCompanyRepository.findById(rolIDRequestDto.getVacancyCompanyId());

            if(optionalVacancyCompanyEntity.isPresent()) {
                vacancyCompanyEntity = optionalVacancyCompanyEntity.get();
            }
        }

        if (roleIDRepository.existsByName(rolIDRequestDto.getName())) {
            throw new EntityAlreadyExistsException();
        }

        RoleIDEntity roleEntityNew = new RoleIDEntity();
        roleEntityNew.setName(rolIDRequestDto.getName());
        roleEntityNew.setDescription(rolIDRequestDto.getDescription());
        roleEntityNew.setVacancyCompanyEntity(vacancyCompanyEntity);

        RoleIDEntity roleIDEntitySave = roleIDRepository.save(roleEntityNew);
        return mapperRolIDResponse.toDto(roleIDEntitySave);
    }

    public RoleIDResponseDto updateRolID(Long id, RoleIDRequestDto rolIDRequestDto) {
        VacancyCompanyEntity vacancyCompanyEntity = null;

        if (rolIDRequestDto.getVacancyCompanyId() != null) {
            Optional<VacancyCompanyEntity> optionalVacancyCompanyEntity = vacancyCompanyRepository.findById(rolIDRequestDto.getVacancyCompanyId());

            if(optionalVacancyCompanyEntity.isPresent()) {
                vacancyCompanyEntity = optionalVacancyCompanyEntity.get();
            }
        }

        RoleIDEntity existingRolId = roleIDRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingRolId.setName(rolIDRequestDto.getName());
        existingRolId.setDescription(rolIDRequestDto.getDescription());
        existingRolId.setVacancyCompanyEntity(vacancyCompanyEntity);
        RoleIDEntity updatedRolId = roleIDRepository.save(existingRolId);

        return mapperRolIDResponse.toDto(updatedRolId);
    }

    public void deleteRolID(Long id) {
        RoleIDEntity existingRolId = roleIDRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        if (postulationRepository.existsByRoleId(id)) {
            throw new EntityAlreadyHasRelationException();
        }

        roleIDRepository.delete(existingRolId);
    }
}
