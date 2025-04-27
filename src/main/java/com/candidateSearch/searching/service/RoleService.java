package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.RoleRequestDto;
import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.CompanyVacancyEntity;
import com.candidateSearch.searching.exception.type.EntityAlreadyExistsException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.mapper.IMapperRole;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.repository.ICompanyVacancyRepository;
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
