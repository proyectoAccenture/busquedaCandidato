package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.RoleRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleResponseDto;
import com.busquedaCandidato.candidato.entity.RoleEntity;
import com.busquedaCandidato.candidato.entity.CompanyVacancyEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperRole;
import com.busquedaCandidato.candidato.repository.IRoleRepository;
import com.busquedaCandidato.candidato.repository.ICompanyVacancyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {

    private final IRoleRepository roleIDRepository;
    private final ICompanyVacancyRepository vacancyCompanyRepository;
    private final IMapperRole mapperRolID;

    public RoleResponseDto getRolID(Long id) {
        return roleIDRepository.findById(id)
                .map(mapperRolID::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<RoleResponseDto> getAllRolID() {
        return roleIDRepository.findAll().stream()
                .map(mapperRolID::toDto)
                .collect(Collectors.toList());
    }

    public RoleResponseDto saveRolID(RoleRequestDto rolIDRequestDto) {

        CompanyVacancyEntity companyVacancyEntity = null;

        if (rolIDRequestDto.getVacancyCompanyId() != null) {
            Optional<CompanyVacancyEntity> optionalVacancyCompanyEntity = vacancyCompanyRepository.findById(rolIDRequestDto.getVacancyCompanyId());

            if(optionalVacancyCompanyEntity.isPresent()) {
                companyVacancyEntity = optionalVacancyCompanyEntity.get();
            }
        }

        if (roleIDRepository.existsByName(rolIDRequestDto.getName())) {
            throw new EntityAlreadyExistsException();
        }

        RoleEntity roleEntityNew = new RoleEntity();
        roleEntityNew.setName(rolIDRequestDto.getName());
        roleEntityNew.setDescription(rolIDRequestDto.getDescription());
        roleEntityNew.setCompanyVacancy(companyVacancyEntity);

        RoleEntity roleEntitySave = roleIDRepository.save(roleEntityNew);

        assert companyVacancyEntity != null;
        companyVacancyEntity.setRole(roleEntitySave);
        vacancyCompanyRepository.save(companyVacancyEntity);

        return mapperRolID.toDto(roleEntitySave);
    }

    public RoleResponseDto updateRolID(Long id, RoleRequestDto rolIDRequestDto) {
        CompanyVacancyEntity companyVacancyEntity = null;

        if (rolIDRequestDto.getVacancyCompanyId() != null) {
            Optional<CompanyVacancyEntity> optionalVacancyCompanyEntity = vacancyCompanyRepository.findById(rolIDRequestDto.getVacancyCompanyId());

            if(optionalVacancyCompanyEntity.isPresent()) {
                companyVacancyEntity = optionalVacancyCompanyEntity.get();
            }
        }

        RoleEntity existingRolId = roleIDRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingRolId.setName(rolIDRequestDto.getName());
        existingRolId.setDescription(rolIDRequestDto.getDescription());
        existingRolId.setCompanyVacancy(companyVacancyEntity);
        RoleEntity updatedRolId = roleIDRepository.save(existingRolId);

        return mapperRolID.toDto(updatedRolId);
    }

    @Transactional
    public void deleteRolID(Long id) {
        RoleEntity existingRolId = roleIDRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        CompanyVacancyEntity vacancy = existingRolId.getCompanyVacancy();
        if(vacancy != null){
            vacancy.setRole(null);
            vacancyCompanyRepository.save(vacancy);
        }
        roleIDRepository.delete(existingRolId);
    }
}
