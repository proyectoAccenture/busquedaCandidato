package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.RoleRequestDto;
import com.candidateSearch.searching.dto.request.validation.validator.PostulationValidator;
import com.candidateSearch.searching.dto.request.validation.validator.RoleValidator;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.exception.globalmessage.GlobalMessage;
import com.candidateSearch.searching.exception.type.BusinessException;
import com.candidateSearch.searching.mapper.IMapperRole;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.entity.utility.Status;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));
    }

    public PaginationResponseDto<RoleResponseDto> getAllRoles(List<Status> statuses, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RoleEntity> rolePage = roleRepository.findByStatusIn(
                RoleValidator.validateStatusesOrDefault(statuses),
                pageable);

        List<RoleResponseDto> dtoList = rolePage.getContent()
                .stream()
                .map(mapperRole::toDto)
                .collect(Collectors.toList());

        return new PaginationResponseDto<>(
                dtoList,
                rolePage.getNumber(),
                rolePage.getSize(),
                rolePage.getTotalPages(),
                rolePage.getTotalElements()
        );
    }

    public PaginationResponseDto<RoleResponseDto> searchRoles(String query,List<Status> statuses, int page, int size){
        RoleValidator.validateQueryNotEmpty(query);
        Pageable pageable = PageRequest.of(page, size);
        Page<RoleEntity> roleEntityList = roleRepository.searchByAllFields(query,RoleValidator.validateStatusesOrDefault(statuses),pageable);
        RoleValidator.validateCandidatePageNotEmpty(roleEntityList);
        List<RoleResponseDto> responseDtoList = roleEntityList.getContent()
                .stream()
                .map(mapperRole::toDto)
                .toList();

        return new PaginationResponseDto<>(
                responseDtoList,
                roleEntityList.getNumber(),
                roleEntityList.getSize(),
                roleEntityList.getTotalPages(),
                roleEntityList.getTotalElements()
        );

    }
    public RoleResponseDto saveRole(RoleRequestDto roleRequestDto) {

        if(roleRequestDto.getStatus().equals(Status.INACTIVE) || roleRequestDto.getStatus().equals(Status.BLOCKED)){
            throw new BusinessException(GlobalMessage.CANNOT_BE_CREATED);
        }

        if (roleRepository.existsByNameRoleAndStatusNot(roleRequestDto.getNameRole(), Status.INACTIVE)) {
            throw new BusinessException(GlobalMessage.CANDIDATE_ALREADY_HAVE_POSTULATION);
        }

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(roleRequestDto.getJobProfile())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        OriginEntity originEntity = originRepository.findById(roleRequestDto.getOrigin())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

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
        roleEntityNew.setStatus(roleRequestDto.getStatus());
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

        if (roleRequestDto.getStatus() == Status.INACTIVE ||
                roleRequestDto.getStatus() == Status.BLOCKED) {
            throw new BusinessException(GlobalMessage.CANNOT_BE_CREATED);
        }

        RoleEntity existingEntity  = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(roleRequestDto.getJobProfile())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        OriginEntity originEntity = originRepository.findById(roleRequestDto.getOrigin())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        existingEntity.setNameRole(roleRequestDto.getNameRole());
        existingEntity.setDescription(roleRequestDto.getDescription());
        existingEntity.setContract(roleRequestDto.getContract());
        existingEntity.setSalary(roleRequestDto.getSalary());
        existingEntity.setLevel(roleRequestDto.getLevel());
        existingEntity.setSeniority(roleRequestDto.getSeniority());
        existingEntity.setSkills(roleRequestDto.getSkills());
        existingEntity.setExperience(roleRequestDto.getExperience());
        existingEntity.setAssignmentTime(roleRequestDto.getAssignmentTime());
        existingEntity.setStatus(Status.ACTIVE);
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
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        existing.setStatus(Status.INACTIVE);
        roleRepository.save(existing);
    }
}
