package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.mapper.IMapperStatusResponse;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StateService {

    @Autowired
    private final IStateRepository stateRepository;

    @Autowired
    private final IMapperStatusResponse mapperStatusResponse;

    public Optional<StateResponseDto> getState(Long id){
        return stateRepository.findById(id)
                .map(mapperStatusResponse::StatusToStatusResponse);
    }
}
