package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateStateRequestDto;
import com.candidateSearch.searching.dto.request.CandidateStateRequestUpdateDto;
import com.candidateSearch.searching.dto.response.CandidateStateResponseDto;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.exception.type.ProcessNoExistException;
import com.candidateSearch.searching.exception.type.StateNoFoundException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.CannotBeCreateCandidateProcessException;
import com.candidateSearch.searching.mapper.IMapperCandidateState;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import com.candidateSearch.searching.repository.IStateRepository;
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
    private final IMapperCandidateState mapperCandidateState;

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

        processEntity.getCandidateState().add(savedEntity);
        processRepository.save(processEntity);

        return mapperCandidateState.toDto(savedEntity);
    }

    public CandidateStateResponseDto getCandidateStateById(Long processId){
         CandidateStateEntity candidateState = candidateStateRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        return mapperCandidateState.toDto(candidateState);
    }

    public List<CandidateStateResponseDto> getAllCandidateState(){
        return candidateStateRepository.findAll().stream()
                .map(mapperCandidateState::toDto)
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

        return Optional.of(mapperCandidateState.toDto(updatedEntity));
    }

    public void deleteCandidateState(Long id){
        CandidateStateEntity existingCandidateState = candidateStateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        candidateStateRepository.delete(existingCandidateState);
    }
}
