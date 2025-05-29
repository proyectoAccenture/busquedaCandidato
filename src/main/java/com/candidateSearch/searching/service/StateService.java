package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.StateRequestDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.exception.type.CustomConflictException;
import com.candidateSearch.searching.exception.type.CustomNotFoundException;
import com.candidateSearch.searching.mapper.IMapperState;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IStateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StateService {

    private final IStateRepository stateRepository;
    private final IMapperState mapperState;
    private final ICandidateStateRepository candidateStateRepository;

    public StateResponseDto getState(Long id){
        return stateRepository.findById(id)
                .map(mapperState::toDto)
                .orElseThrow(()-> new CustomNotFoundException("There is no state with that ID."));
    }

    public List<StateResponseDto> getAllState(){
        return stateRepository.findAll().stream()
                .map(mapperState::toDto)
                .collect(Collectors.toList());
    }

    public StateResponseDto saveState(StateRequestDto stateRequestDto) {
        if(stateRepository.existsByName(stateRequestDto.getName())){
            throw new CustomConflictException("A state with that ID already exists.");
        }

        StateEntity stateEntity = mapperState.toEntity(stateRequestDto);
        StateEntity stateEntitySave = stateRepository.save(stateEntity);

        return mapperState.toDto(stateEntitySave);
    }

    public StateResponseDto updateState(Long id, StateRequestDto stateRequestDto) {
        StateEntity existingState = stateRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no state with that ID."));

        existingState.setName(stateRequestDto.getName());
        StateEntity updatedState = stateRepository.save(existingState);

        return mapperState.toDto(updatedState);
    }

    @Transactional
    public void deleteState(Long id){
        StateEntity existingState = stateRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no state with that ID."));

        List<CandidateStateEntity> candidateStateExist = candidateStateRepository.findAllByStateId(id);
        if(candidateStateExist != null && !candidateStateExist.isEmpty()){
            for (CandidateStateEntity candidateState : candidateStateExist){
                candidateState.setState(null);
                candidateStateRepository.save(candidateState);
            }
        }
        stateRepository.delete(existingState);
    }
}
