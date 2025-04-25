package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileRequest;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileResponse;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobProfileService {
    private final IJobProfileRepository jobProfileRepository;
    private final IMapperJobProfileResponse mapperJobProfileResponse;
    private final IMapperJobProfileRequest mapperJobProfileRequest;

    public JobProfileResponseDto getJobProfile(Long id){
        return jobProfileRepository.findById(id)
                .map(mapperJobProfileResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<JobProfileResponseDto> getAllJobProfile(){
        return jobProfileRepository.findAll().stream()
                .map(mapperJobProfileResponse::toDto)
                .collect(Collectors.toList());
    }

    public JobProfileResponseDto saveJobProfile(JobProfileRequestDto jobProfileRequestDto) {
        if(jobProfileRepository.existsByName(jobProfileRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }

        JobProfileEntity jobProfileEntity = mapperJobProfileRequest.toEntity(jobProfileRequestDto);
        JobProfileEntity jobProfileEntitySave = jobProfileRepository.save(jobProfileEntity);

        return mapperJobProfileResponse.toDto(jobProfileEntitySave);
    }

    public JobProfileResponseDto updateJobProfile(Long id, JobProfileRequestDto jobProfileRequestDto) {
        JobProfileEntity existingJob = jobProfileRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingJob.setName(jobProfileRequestDto.getName());
        JobProfileEntity updatedJob = jobProfileRepository.save(existingJob);

        return mapperJobProfileResponse.toDto(updatedJob);
    }

    public void deleteJobProfile(Long id){
        JobProfileEntity existingJob = jobProfileRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        jobProfileRepository.delete(existingJob);
    }
}
