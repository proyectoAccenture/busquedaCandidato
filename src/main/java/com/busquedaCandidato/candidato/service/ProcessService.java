package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.exception.type.CandidateNoPostulationException;
import com.busquedaCandidato.candidato.exception.type.ProcessAlreadyExistException;
import com.busquedaCandidato.candidato.mapper.IMapperProcessResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
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
    private final IPostulationRepository postulationRepository;
    private final IMapperProcessResponse mapperProcessResponse;

     public Optional<ProcessResponseDto> getProcesByIdCandidate(Long id){
        CandidateEntity candidateEntity = candidateRepository.findById(id).get();
        return processRepository.findById(candidateEntity.getId())
                .map(mapperProcessResponse::ProcessToProcessResponse);
    }



    public Optional<ProcessResponseDto> getByIdProcess(Long id){
        return processRepository.findById(id)
                .map(mapperProcessResponse::ProcessToProcessResponse);
    }

    public List<ProcessResponseDto> getAllProcess(){
        return processRepository.findAll().stream()
                .map(mapperProcessResponse::ProcessToProcessResponse)
                .collect(Collectors.toList());
    }

    public ProcessResponseDto saveProcess(ProcessRequestDto processRequestDto) {

        ProcessEntity processEntityExist = processRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(ProcessAlreadyExistException::new);

        PostulationEntity postulationEntity = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        ProcessEntity newCandidateStatusHistory = new ProcessEntity();
        newCandidateStatusHistory.setDescription(processRequestDto.getDescription());
        newCandidateStatusHistory.setAssignmentDate(processRequestDto.getAssignedDate());
        newCandidateStatusHistory.setPostulation(postulationEntity);

        ProcessEntity processEntitySave = processRepository.save(newCandidateStatusHistory);
        return mapperProcessResponse.ProcessToProcessResponse(processEntitySave);
    }

    public Optional<ProcessResponseDto> updateProcess(Long id, ProcessRequestDto processRequestDto) {
        return processRepository.findById(id)
                .map(existingEntity -> {

                    PostulationEntity postulation = postulationRepository.findById(processRequestDto.getPostulationId())
                            .orElseThrow(CandidateNoPostulationException::new);


                    existingEntity.setPostulation(postulation);
                    existingEntity.setDescription(processRequestDto.getDescription());
                    existingEntity.setAssignmentDate(processRequestDto.getAssignedDate());
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
