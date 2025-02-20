package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.response.AddStateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StateService {

    @Autowired
    private final IStateRepository stateRepository;


}
