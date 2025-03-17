package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileRequest;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileResponse;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobProfileService {
    private final IJobProfileRepository jobProfileRepository;
    private final IMapperJobProfileResponse mapperJobProfileResponse;
    private final IMapperJobProfileRequest mapperJobProfileRequest;

    public Optional<JobProfileResponseDto> getJobProfile(Long id){
        return jobProfileRepository.findById(id)
                .map(mapperJobProfileResponse::JobProfileToJobProfileResponse);

    }

    public List<JobProfileResponseDto> getAllJobProfile(){
        return jobProfileRepository.findAll().stream()
                .map(mapperJobProfileResponse::JobProfileToJobProfileResponse)
                .collect(Collectors.toList());
    }

    public JobProfileResponseDto saveJobProfile(JobProfileRequestDto jobProfileRequestDto) {
        if(jobProfileRepository.existsByName(jobProfileRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }
        JobProfileEntity jobProfileEntity = mapperJobProfileRequest.JobProfileResquestToJobProfile(jobProfileRequestDto);
        JobProfileEntity jobProfileEntitySave = jobProfileRepository.save(jobProfileEntity);
        return mapperJobProfileResponse.JobProfileToJobProfileResponse(jobProfileEntitySave);
    }

    public Optional<JobProfileResponseDto> updateJobProfile(Long id, JobProfileRequestDto jobProfileRequestDto) {
        return jobProfileRepository.findById(id)
                .map(existingJob -> {
                    existingJob.setName(jobProfileRequestDto.getName());
                    return mapperJobProfileResponse.JobProfileToJobProfileResponse(jobProfileRepository.save(existingJob));
                });
    }

    public boolean deleteJobProfile(Long id){
        if (jobProfileRepository.existsById(id)) {
            jobProfileRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
