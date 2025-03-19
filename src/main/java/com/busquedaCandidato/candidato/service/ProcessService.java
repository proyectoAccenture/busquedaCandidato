package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.exception.type.CandidateNoExistException;
import com.busquedaCandidato.candidato.exception.type.CandidateNoPostulationException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
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

     public ProcessResponseDto getProcessByIdCandidate(Long id){
        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        return processRepository.findById(candidateEntity.getId())
                .map(mapperProcessResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public ProcessResponseDto getByIdProcess(Long id){
        return processRepository.findById(id)
                .map(mapperProcessResponse::toDto)
                .orElseThrow(CandidateNoExistException::new);
    }

    public List<ProcessResponseDto> getAllProcess(){
        return processRepository.findAll().stream()
                .map(mapperProcessResponse::toDto)
                .collect(Collectors.toList());
    }

    public ProcessResponseDto saveProcess(ProcessRequestDto processRequestDto) {

        processRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(ProcessAlreadyExistException::new);

        PostulationEntity postulationEntity = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        ProcessEntity newCandidateStatusHistory = new ProcessEntity();
        newCandidateStatusHistory.setDescription(processRequestDto.getDescription());
        newCandidateStatusHistory.setAssignmentDate(processRequestDto.getAssignedDate());
        newCandidateStatusHistory.setPostulation(postulationEntity);
        ProcessEntity processEntitySave = processRepository.save(newCandidateStatusHistory);

        return mapperProcessResponse.toDto(processEntitySave);
    }

    public Optional<ProcessResponseDto> updateProcess(Long id, ProcessRequestDto processRequestDto) {
        ProcessEntity existingEntity  = processRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        PostulationEntity postulation = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        existingEntity.setPostulation(postulation);
        existingEntity.setDescription(processRequestDto.getDescription());
        existingEntity.setAssignmentDate(processRequestDto.getAssignedDate());

        return Optional.of(mapperProcessResponse.toDto(processRepository.save(existingEntity)));
    }

    public void deleteProcess(Long id){
        PostulationEntity existingPostulation = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        postulationRepository.delete(existingPostulation);
    }
}
