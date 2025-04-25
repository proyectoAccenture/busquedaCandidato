package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateStateRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidateStateRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidateStateResponseDto;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.CandidateStateEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.ProcessNoExistException;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.CannotBeCreateCandidateProcessException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateStateResponse;
import com.busquedaCandidato.candidato.repository.ICandidateStateRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateStateService {

    private final IProcessRepository processRepository;
    private final IStateRepository stateRepository;
    private final ICandidateStateRepository candidateStateRepository;
    private final IMapperCandidateStateResponse mapperCandidateStateResponse;

    public CandidateStateResponseDto addStateToProcess(CandidateStateRequestDto candidateStateRequestDto){

        ProcessEntity processEntity = processRepository.findById(candidateStateRequestDto.getProcessId())
                .orElseThrow(ProcessNoExistException::new);

        Optional<CandidateStateEntity> currentStateOptional = candidateStateRepository.findTopByProcessOrderByIdDesc(processEntity);

        if (currentStateOptional.isPresent()) {
            CandidateStateEntity lastCandidateState = currentStateOptional.get();

            if (!lastCandidateState.getStatus()) {
                throw new CannotBeCreateCandidateProcessException();
            }
        }

        StateEntity stateEntity = stateRepository.findById(candidateStateRequestDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        CandidateStateEntity newCandidateProcess = new CandidateStateEntity();
        newCandidateProcess.setProcess(processEntity);
        newCandidateProcess.setState(stateEntity);
        newCandidateProcess.setDescription(candidateStateRequestDto.getDescription());
        newCandidateProcess.setStatus(candidateStateRequestDto.getStatus());
        newCandidateProcess.setAssignedDate(candidateStateRequestDto.getAssignedDate());

        CandidateStateEntity savedEntity = candidateStateRepository.save(newCandidateProcess);
        return mapperCandidateStateResponse.toDto(savedEntity);
    }

    public CandidateStateResponseDto getCandidateStateById(Long processId){
         CandidateStateEntity candidateState = candidateStateRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        return mapperCandidateStateResponse.toDto(candidateState);
    }

    public List<CandidateStateResponseDto> getAllCandidateState(){
        return candidateStateRepository.findAll().stream()
                .map(mapperCandidateStateResponse::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CandidateStateResponseDto> updateCandidateState(Long id, CandidateStateRequestUpdateDto candidateStateRequestUpdateDto) {
        CandidateStateEntity existingEntity  = candidateStateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        StateEntity newState = stateRepository.findById(candidateStateRequestUpdateDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        existingEntity.setState(newState);
        existingEntity.setDescription(candidateStateRequestUpdateDto.getDescription());
        existingEntity.setStatus(candidateStateRequestUpdateDto.getStatus());
        existingEntity.setAssignedDate(candidateStateRequestUpdateDto.getAssignedDate());
        CandidateStateEntity updatedEntity = candidateStateRepository.save(existingEntity);

        return Optional.of(mapperCandidateStateResponse.toDto(updatedEntity));
    }

    public void deleteCandidateState(Long id){
        CandidateStateEntity existingCandidateState = candidateStateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        candidateStateRepository.delete(existingCandidateState);
    }
}
