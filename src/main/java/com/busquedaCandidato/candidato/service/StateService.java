package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperStateRequest;
import com.busquedaCandidato.candidato.mapper.IMapperStateResponse;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StateService {

    private final IStateRepository stateRepository;
    private final IMapperStateResponse mapperStateResponse;
    private final IMapperStateRequest mapperStateRequest;

    public Optional<StateResponseDto> getState(Long id){
        return stateRepository.findById(id)
                .map(mapperStateResponse::StateToStateResponse);

    }

    public List<StateResponseDto> getAllState(){
        return stateRepository.findAll().stream()
                .map(mapperStateResponse::StateToStateResponse)
                .collect(Collectors.toList());
    }

    public StateResponseDto saveState(StateRequestDto stateRequestDto) {
        if(stateRepository.existsByName(stateRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }
        StateEntity stateEntity = mapperStateRequest.StateResquestToState(stateRequestDto);
        StateEntity stateEntitySave = stateRepository.save(stateEntity);
        return mapperStateResponse.StateToStateResponse(stateEntitySave);
    }

    public Optional<StateResponseDto> updateState(Long id, StateRequestDto stateRequestDto) {
        return stateRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(stateRequestDto.getName());
                    return mapperStateResponse.StateToStateResponse(stateRepository.save(existingEntity));
                });
    }

    public boolean deleteState(Long id){
        if (stateRepository.existsById(id)) {
            stateRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
