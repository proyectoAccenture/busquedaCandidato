package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.StateAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileRequest;
import com.busquedaCandidato.candidato.mapper.IMapperJobProfileResponse;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para manejar las operaciones relacionadas con el perfil de trabajo.
 */
@Service
@AllArgsConstructor
public class JobProfileService {
    private final IJobProfileRepository jobProfileRepository;
    private final IMapperJobProfileResponse mapperJobProfileResponse;
    private final IMapperJobProfileRequest mapperJobProfileRequest;


    /**
     * Obtiene un perfil de trabajo por su ID.
     *
     * @param id El ID del perfil de trabajo.
     * @return Un Optional que contiene el JobProfileResponseDto si se encuentra, de lo contrario vacío.
     */
    public Optional<JobProfileResponseDto> getJobProfile(Long id){
        return jobProfileRepository.findById(id)
                .map(mapperJobProfileResponse::JobProfileToStatusResponse);

    }

    /**
     * Obtiene todos los perfiles de trabajo.
     *
     * @return Una lista de JobProfileResponseDto.
     */
    public List<JobProfileResponseDto> getAllJobProfile(){
        return jobProfileRepository.findAll().stream()
                .map(mapperJobProfileResponse::JobProfileToStatusResponse)
                .collect(Collectors.toList());
    }



    /**
     * Guarda un nuevo perfil de trabajo.
     *
     * @param jobProfileRequestDto El DTO que representa la solicitud de creación de un perfil de trabajo.
     * @return El JobProfileResponseDto del perfil de trabajo guardado.
     * @throws StateAlreadyExistsException si ya existe un perfil de trabajo con el mismo nombre.
     */
    public JobProfileResponseDto saveJobProfile(JobProfileRequestDto jobProfileRequestDto) {
        if(jobProfileRepository.existsByName(jobProfileRequestDto.getName())){
            throw new StateAlreadyExistsException();
        }
        JobProfileEntity jobProfileEntity = mapperJobProfileRequest.JobProfileResquestToStatus(jobProfileRequestDto);
        JobProfileEntity jobProfileEntitySave = jobProfileRepository.save(jobProfileEntity);
        return mapperJobProfileResponse.JobProfileToStatusResponse(jobProfileEntitySave);
    }


    /**
     * Actualiza un perfil de trabajo existente.
     *
     * @param id El ID del perfil de trabajo.
     * @param jobProfileRequestDto El DTO que representa la solicitud de actualización de un perfil de trabajo.
     * @return Un Optional que contiene el JobProfileResponseDto actualizado si se encuentra, de lo contrario vacío.
     */
    public Optional<JobProfileResponseDto> updateJobProfile(Long id, JobProfileRequestDto jobProfileRequestDto) {
        return jobProfileRepository.findById(id)
                .map(existingJob -> {
                    existingJob.setName(jobProfileRequestDto.getName());
                    return mapperJobProfileResponse.JobProfileToStatusResponse(jobProfileRepository.save(existingJob));
                });
    }


    /**
     * Elimina un perfil de trabajo por su ID.
     *
     * @param id El ID del perfil de trabajo.
     * @return true si el perfil de trabajo fue eliminado, de lo contrario false.
     */
    public boolean deleteJobProfile(Long id){
        if (jobProfileRepository.existsById(id)) {
            jobProfileRepository.deleteById(id);
            return true;
        }
        return false;
    }

}//Fin de la clase JobProfileService
