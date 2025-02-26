package com.busquedaCandidato.candidato.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.exception.type.StateAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileRequest;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileResponse;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobProfileService {

    private final IJobProfileRepository jobprofileRepository;
    private final IMapperJobProfileResponse mapperJobProfileResponse;
    private final IMapperJobProfileRequest mapperJobProfileRequest;

    public Optional<JobProfileResponseDto> getJobProfile(Long id){
        return jobprofileRepository.findById(id)
                .map(mapperJobProfileResponse::JobProfileToJobProfileResponse);

    }

    public List<JobProfileResponseDto> getAllJobProfile(){
        return jobprofileRepository.findAll().stream()
                .map(mapperJobProfileResponse::JobProfileToJobProfileResponse)
                .collect(Collectors.toList());
    }

    public JobProfileResponseDto saveJobProfile(JobProfileRequestDto jobprofileRequestDto) {
        if(jobprofileRepository.existsByName(jobprofileRequestDto.getName())){
            throw new StateAlreadyExistsException();
        }
        JobProfileEntity jobprofileEntity = mapperJobProfileRequest.JobProfileRequestToJobProfile(jobprofileRequestDto);
        JobProfileEntity jobprofileEntitySave = jobprofileRepository.save(jobprofileEntity);
        return mapperJobProfileResponse.JobProfileToJobProfileResponse(jobprofileEntitySave);
    }

    public Optional<JobProfileResponseDto> updateJobProfile(Long id, JobProfileRequestDto jobprofileRequestDto) {
        return jobprofileRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(jobprofileRequestDto.getName());
                    return mapperJobProfileResponse.JobProfileToJobProfileResponse(jobprofileRepository.save(existingEntity));
                });
    }

    public boolean deleteJobProfile(Long id){
        if (jobprofileRepository.existsById(id)) {
            jobprofileRepository.deleteById(id);
            return true;
        }
        return false;
}
}