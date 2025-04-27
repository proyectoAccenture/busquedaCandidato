package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperState;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StateService {

    private final IStateRepository stateRepository;
    private final IMapperState mapperState;

    public StateResponseDto getState(Long id){
        return stateRepository.findById(id)
                .map(mapperState::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<StateResponseDto> getAllState(){
        return stateRepository.findAll().stream()
                .map(mapperState::toDto)
                .collect(Collectors.toList());
    }

    public StateResponseDto saveState(StateRequestDto stateRequestDto) {
        if(stateRepository.existsByName(stateRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }

        StateEntity stateEntity = mapperState.toEntity(stateRequestDto);
        StateEntity stateEntitySave = stateRepository.save(stateEntity);

        return mapperState.toDto(stateEntitySave);
    }

    public StateResponseDto updateState(Long id, StateRequestDto stateRequestDto) {
        StateEntity existingState = stateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingState.setName(stateRequestDto.getName());
        StateEntity updatedState = stateRepository.save(existingState);

        return mapperState.toDto(updatedState);
    }

    public void deleteState(Long id){
        StateEntity existingState = stateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        stateRepository.delete(existingState);
    }
}
