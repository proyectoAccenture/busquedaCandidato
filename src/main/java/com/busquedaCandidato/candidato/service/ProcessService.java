package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.exception.type.CandidateNoPostulation;
import com.busquedaCandidato.candidato.exception.type.CannotBeCreateCandidateProcessException;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.mapper.IMapperProcessResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProcessService {

    private final IProcessRepository processRepository;
    private final ICandidateRepository candidateRepository;
    private final IPostulationRepository postulationRepository;
    private final IMapperProcessResponse mapperProcessResponse;

    public Optional<ProcessResponseDto> getProcess(Long id){
        return processRepository.findById(id)
                .map(mapperProcessResponse::ProcessToProcessResponse);
    }

    public List<ProcessResponseDto> getAllProcess(){
        return processRepository.findAll().stream()
                .map(mapperProcessResponse::ProcessToProcessResponse)
                .collect(Collectors.toList());
    }

    public ProcessResponseDto saveProcess(ProcessRequestDto processRequestDto) {

        Optional<PostulationEntity> postulationEntityOptional = postulationRepository.findById(processRequestDto.getCandidateId());

        if(postulationEntityOptional.isEmpty()){
            throw new CandidateNoPostulation();
        }

        CandidateEntity candidate = candidateRepository.findById(processRequestDto.getCandidateId())
                .orElseThrow(EntityNotFoundException::new);

        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setCandidate(candidate);
        ProcessEntity processEntitySave = processRepository.save(processEntity);
        return mapperProcessResponse.ProcessToProcessResponse(processEntitySave);
    }

    public Optional<ProcessResponseDto> updateProcess(Long id, ProcessRequestDto processRequestDto) {
        return processRepository.findById(id)
                .map(existingEntity -> {

                    CandidateEntity candidateEntity = candidateRepository.findById(processRequestDto.getCandidateId())
                            .orElseThrow(StateNoFoundException::new);

                    existingEntity.setCandidate(candidateEntity);
                    return mapperProcessResponse.ProcessToProcessResponse(processRepository.save(existingEntity));
                });
    }

    public boolean deleteProcess(Long id){
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
