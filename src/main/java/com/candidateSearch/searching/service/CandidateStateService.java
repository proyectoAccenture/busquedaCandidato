package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateStateRequestDto;
import com.candidateSearch.searching.dto.request.CandidateStateRequestUpdateDto;
import com.candidateSearch.searching.dto.response.CandidateStateResponseDto;
import com.candidateSearch.searching.dto.response.NextValidStatesResponseDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.exception.type.ProcessNoExistException;
import com.candidateSearch.searching.exception.type.StateNoFoundException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.CannotBeCreateCandidateProcessException;
import com.candidateSearch.searching.mapper.IMapperCandidateState;
import com.candidateSearch.searching.mapper.IMapperState;
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
    private final IMapperState mapperState;
    private final StateTransitionManager stateTransitionManager;

    public CandidateStateResponseDto addStateToProcess(CandidateStateRequestDto candidateStateRequestDto){

        ProcessEntity processEntity = processRepository.findById(candidateStateRequestDto.getProcessId())
                .orElseThrow(ProcessNoExistException::new);

        Optional<CandidateStateEntity> currentStateOptional = candidateStateRepository.findTopByProcessOrderByIdDesc(processEntity);

        if (currentStateOptional.isPresent()) {
            CandidateStateEntity lastCandidateState = currentStateOptional.get();

            if (!lastCandidateState.getStatus()) {
                throw new CannotBeCreateCandidateProcessException();
            }
            Long fromStateId = lastCandidateState.getState().getId();

            Long toStateId = candidateStateRequestDto.getStateId();

            stateTransitionManager.validateTransition(fromStateId, toStateId);
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

        return mapperCandidateState.toDto(savedEntity);
    }

    public NextValidStatesResponseDto getNextValidStatesWithInfo(Long processId) {
        ProcessEntity process = processRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        NextValidStatesResponseDto response = new NextValidStatesResponseDto();
        response.setProcessId(process.getId());
        response.setProcessDescription(process.getDescription());

        CandidateEntity candidate = process.getPostulation().getCandidate();
        response.setCandidateId(candidate.getId());
        response.setCandidateName(candidate.getName());
        response.setCandidateLastName(candidate.getLastName());

        Optional<CandidateStateEntity> currentState = candidateStateRepository.findTopByProcessOrderByIdDesc(process);

        List<StateEntity> validStates;
        if (currentState.isEmpty()) {
            validStates = List.of(stateRepository.findById(1L)
                    .orElseThrow(StateNoFoundException::new));
        } else {
            Long currentStateId = currentState.get().getState().getId();
            List<Long> nextStateIds = stateTransitionManager.getNextValidStateIds(currentStateId);
            validStates = stateRepository.findAllById(nextStateIds);
        }

        List<StateResponseDto> stateResponseDto = validStates.stream()
                .map(mapperState::toDto)
                .collect(Collectors.toList());

        response.setNextValidStates(stateResponseDto);

        return response;
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
        CandidateStateEntity existingEntity = candidateStateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        Long currentStateId = existingEntity.getState().getId();
        Long newStateId = candidateStateRequestUpdateDto.getStateId();

        if (!currentStateId.equals(newStateId)) {
            stateTransitionManager.validateTransition(currentStateId, newStateId);
        }

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
