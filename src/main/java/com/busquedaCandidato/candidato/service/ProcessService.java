package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.mapper.IMapperProcessRequest;
import com.busquedaCandidato.candidato.mapper.IMapperProcessResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
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
    private final IMapperProcessResponse mapperProcessResponse;
    private final IMapperProcessRequest iMapperProcessRequest;

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
        ProcessEntity processEntity = iMapperProcessRequest.ProcessResquestToProcessEntity(processRequestDto);
        ProcessEntity processEntitySave = processRepository.save(processEntity);
        return mapperProcessResponse.ProcessToProcessResponse(processEntitySave);
    }

    public Optional<ProcessResponseDto> updateProcess(Long id, ProcessRequestDto processRequestDto) {
        return processRepository.findById(id)
                .map(existingEntity -> {

                    CandidateEntity candidateEntity = candidateRepository.findById(id)
                            .orElseThrow(StateNoFoundException::new);

                    existingEntity.setId(candidateEntity.getId());
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
