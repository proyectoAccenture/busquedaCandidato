package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.RoleRequestDto;
import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.FieldAlreadyExistException;
import com.candidateSearch.searching.mapper.IMapperRole;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final IRoleRepository roleRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperRole mapperRole;

    public RoleResponseDto getRole(Long id){
        return roleRepository.findById(id)
                .map(mapperRole::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<RoleResponseDto> getAllRoles(){
        return roleRepository.findAll().stream()
                .map(mapperRole::toDto)
                .collect(Collectors.toList());
    }

    public RoleResponseDto saveRole(RoleRequestDto roleRequestDto) {
        if(roleRepository.existsByNameRole(roleRequestDto.getNameRole())){
            throw new FieldAlreadyExistException("role name");
        }

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(roleRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(roleRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        RoleEntity roleEntityNew = new RoleEntity();
        roleEntityNew.setNameRole(roleRequestDto.getNameRole());
        roleEntityNew.setDescription(roleRequestDto.getDescription());
        roleEntityNew.setContract(roleRequestDto.getContract());
        roleEntityNew.setSalary(roleRequestDto.getSalary());
        roleEntityNew.setLevel(roleRequestDto.getLevel());
        roleEntityNew.setSeniority(roleRequestDto.getSeniority());
        roleEntityNew.setSkills(roleRequestDto.getSkills());
        roleEntityNew.setExperience(roleRequestDto.getExperience());
        roleEntityNew.setAssignmentTime(roleRequestDto.getAssignmentTime());
        roleEntityNew.setJobProfile(jobProfileEntity);
        roleEntityNew.setOrigin(originEntity);

        RoleEntity vacancyEntitySave = roleRepository.save(roleEntityNew);

        jobProfileEntity.getVacancies().add(vacancyEntitySave);
        jobProfileRepository.save(jobProfileEntity);

        originEntity.getRoles().add(vacancyEntitySave);
        originRepository.save(originEntity);

        return mapperRole.toDto(vacancyEntitySave);
    }

    public Optional<RoleResponseDto> updateRole(Long id, RoleRequestDto roleRequestDto) {
        RoleEntity existingEntity  = roleRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(roleRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(roleRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        existingEntity.setNameRole(roleRequestDto.getNameRole());
        existingEntity.setDescription(roleRequestDto.getDescription());
        existingEntity.setContract(roleRequestDto.getContract());
        existingEntity.setSalary(roleRequestDto.getSalary());
        existingEntity.setLevel(roleRequestDto.getLevel());
        existingEntity.setSeniority(roleRequestDto.getSeniority());
        existingEntity.setSkills(roleRequestDto.getSkills());
        existingEntity.setExperience(roleRequestDto.getExperience());
        existingEntity.setAssignmentTime(roleRequestDto.getAssignmentTime());
        existingEntity.setJobProfile(jobProfileEntity);
        existingEntity.setOrigin(originEntity);

        RoleEntity vacancyEntitySave = roleRepository.save(existingEntity);

        jobProfileEntity.getVacancies().add(vacancyEntitySave);
        jobProfileRepository.save(jobProfileEntity);

        originEntity.getRoles().add(vacancyEntitySave);
        originRepository.save(originEntity);

        return Optional.of(mapperRole.toDto(vacancyEntitySave));
    }

    public void deleteRole(Long id){
        RoleEntity existing = roleRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        roleRepository.delete(existing);
    }
}
