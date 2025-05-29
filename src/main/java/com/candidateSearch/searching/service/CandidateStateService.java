package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.validation.validator.CandidateStateValidator;
import com.candidateSearch.searching.dto.request.validation.validator.CandidateValidator;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.exception.type.*;
import com.candidateSearch.searching.service.state.StateTransitionManager;
import com.candidateSearch.searching.dto.request.CandidateStateRequestDto;
import com.candidateSearch.searching.dto.request.CandidateStateRequestUpdateDto;
import com.candidateSearch.searching.dto.response.CandidateStateResponseDto;
import com.candidateSearch.searching.dto.response.NextValidStatesResponseDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.mapper.IMapperCandidateState;
import com.candidateSearch.searching.mapper.IMapperState;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import com.candidateSearch.searching.repository.IStateRepository;
import com.candidateSearch.searching.entity.utility.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        if(candidateStateRequestDto.getStatusHistory().equals(Status.INACTIVE) || candidateStateRequestDto.getStatusHistory().equals(Status.BLOCKED)){
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        ProcessEntity processEntity = processRepository.findById(candidateStateRequestDto.getProcessId())
                .orElseThrow(()->new CustomNotFoundException("There is not process with this ID."));

        Optional<CandidateStateEntity> currentStateOptional = candidateStateRepository.findTopByProcessAndStatusHistoryOrderByIdDesc(processEntity, Status.ACTIVE);

        if (currentStateOptional.isPresent()) {
            CandidateStateEntity lastCandidateState = currentStateOptional.get();

            if (!Boolean.TRUE.equals(lastCandidateState.getStatus()) ||
                    !Status.ACTIVE.equals(lastCandidateState.getStatusHistory())) {
                throw new CustomBadRequestException("Cannot be create or update, valid the status.");
            }
            Long fromStateId = lastCandidateState.getState().getId();

            Long toStateId = candidateStateRequestDto.getStateId();

            stateTransitionManager.validateTransition(fromStateId, toStateId);
        }

        StateEntity stateEntity = stateRepository.findById(candidateStateRequestDto.getStateId())
                .orElseThrow(()-> new CustomNotFoundException("There is no state with that ID."));

        CandidateStateEntity newCandidateProcess = new CandidateStateEntity();
        newCandidateProcess.setProcess(processEntity);
        newCandidateProcess.setState(stateEntity);
        newCandidateProcess.setDescription(candidateStateRequestDto.getDescription());
        newCandidateProcess.setStatus(candidateStateRequestDto.getStatus());
        newCandidateProcess.setStatusHistory(candidateStateRequestDto.getStatusHistory());
        newCandidateProcess.setAssignedDate(candidateStateRequestDto.getAssignedDate());

        CandidateStateEntity savedEntity = candidateStateRepository.save(newCandidateProcess);

        return mapperCandidateState.toDto(savedEntity);
    }

    public NextValidStatesResponseDto getNextValidStates(Long processId) {
        ProcessEntity process = processRepository.findById(processId)
                .orElseThrow(()-> new CustomNotFoundException("There is no process with that ID."));

        NextValidStatesResponseDto response = new NextValidStatesResponseDto();
        response.setProcessId(process.getId());
        response.setProcessDescription(process.getDescription());

        CandidateEntity candidate = process.getPostulation().getCandidate();
        response.setCandidateId(candidate.getId());
        response.setCandidateName(candidate.getName());
        response.setCandidateLastName(candidate.getLastName());

        Optional<CandidateStateEntity> currentState = candidateStateRepository
                .findTopByProcessAndStatusHistoryOrderByIdDesc(process, Status.ACTIVE);

        List<StateEntity> validStates;
        if (currentState.isEmpty()) {
            validStates = List.of(stateRepository.findById(1L)
                    .orElseThrow(()-> new CustomNotFoundException("There is no state with that ID.")));
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
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that process ID."));

        return mapperCandidateState.toDto(candidateState);
    }

    public List<CandidateStateResponseDto> getAllCandidateState(){
        return candidateStateRepository.findAll().stream()
                .filter(cs -> cs.getStatusHistory().equals(Status.ACTIVE))
                .map(mapperCandidateState::toDto)
                .collect(Collectors.toList());
    }

    public PaginationResponseDto<CandidateStateResponseDto> getAllCandidateStates( List<Status> statuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<CandidateStateEntity> entityPage = candidateStateRepository.findByStatusHistoryIn(
                CandidateStateValidator.validateStatusesOrDefault(statuses),
                pageable);

        List<CandidateStateResponseDto> content = entityPage.getContent().stream()
                .map(mapperCandidateState::toDto)
                .toList();

        return new PaginationResponseDto<>(
                content,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalPages(),
                entityPage.getTotalElements()
        );
    }


    public Optional<CandidateStateResponseDto> updateCandidateState(Long id, CandidateStateRequestUpdateDto candidateStateRequestUpdateDto) {

        if(candidateStateRequestUpdateDto.getStatusHistory().equals(Status.BLOCKED) || candidateStateRequestUpdateDto.getStatusHistory().equals(Status.INACTIVE)){
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        CandidateStateEntity existingEntity = candidateStateRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that ID."));

        Long currentStateId = existingEntity.getState().getId();
        Long newStateId = candidateStateRequestUpdateDto.getStateId();

        if (!currentStateId.equals(newStateId)) {
            stateTransitionManager.validateTransition(currentStateId, newStateId);
        }

        if (existingEntity.getProcess().getStatus() != Status.ACTIVE) {
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        StateEntity newState = stateRepository.findById(candidateStateRequestUpdateDto.getStateId())
                .orElseThrow(()->new CustomNotFoundException("There is no state with that ID."));

        existingEntity.setState(newState);
        existingEntity.setDescription(candidateStateRequestUpdateDto.getDescription());
        existingEntity.setStatus(candidateStateRequestUpdateDto.getStatus());
        existingEntity.setStatusHistory(candidateStateRequestUpdateDto.getStatusHistory());
        existingEntity.setAssignedDate(candidateStateRequestUpdateDto.getAssignedDate());

        CandidateStateEntity updatedEntity = candidateStateRepository.save(existingEntity);

        return Optional.of(mapperCandidateState.toDto(updatedEntity));
    }

    @Transactional
    public void deleteCandidateState(Long id){
        CandidateStateEntity existingCandidateState = candidateStateRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that ID."));

        existingCandidateState.setStatusHistory(Status.INACTIVE);
        candidateStateRepository.save(existingCandidateState);
    }
}
