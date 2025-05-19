package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.JobProfileRequestDto;
import com.candidateSearch.searching.dto.response.JobProfileResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.EntityAlreadyExistsException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.mapper.IMapperJobProfile;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobProfileService {
    private final IJobProfileRepository jobProfileRepository;
    private final ICandidateRepository candidateRepository;
    private final IRoleRepository roleRepository;
    private final IMapperJobProfile mapperJobProfile;

    public JobProfileResponseDto getJobProfile(Long id){
        return jobProfileRepository.findById(id)
                .map(mapperJobProfile::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<JobProfileResponseDto> getAllJobProfile(){
        return jobProfileRepository.findAll().stream()
                .map(mapperJobProfile::toDto)
                .collect(Collectors.toList());
    }

    public JobProfileResponseDto saveJobProfile(JobProfileRequestDto jobProfileRequestDto) {
        if(jobProfileRepository.existsByName(jobProfileRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }

        JobProfileEntity jobProfileEntity = mapperJobProfile.toEntity(jobProfileRequestDto);
        JobProfileEntity jobProfileEntitySave = jobProfileRepository.save(jobProfileEntity);

        return mapperJobProfile.toDto(jobProfileEntitySave);
    }

    public JobProfileResponseDto updateJobProfile(Long id, JobProfileRequestDto jobProfileRequestDto) {
        JobProfileEntity existingJob = jobProfileRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingJob.setName(jobProfileRequestDto.getName());
        JobProfileEntity updatedJob = jobProfileRepository.save(existingJob);

        return mapperJobProfile.toDto(updatedJob);
    }

    @Transactional
    public void deleteJobProfile(Long id){
        JobProfileEntity existingJob = jobProfileRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        List<CandidateEntity> candidates = existingJob.getCandidates();
        if (candidates != null && !candidates.isEmpty()) {
            for (CandidateEntity candidate : candidates) {
                candidate.setJobProfile(null);
                candidateRepository.save(candidate);
            }
        }

        List<RoleEntity> rolesExist = roleRepository.findAllByJobProfileId(id);
        if(rolesExist != null && !rolesExist.isEmpty()){
            for (RoleEntity role : rolesExist){
                role.setJobProfile(null);
                roleRepository.save(role);
            }
        }
        jobProfileRepository.delete(existingJob);
    }
}
