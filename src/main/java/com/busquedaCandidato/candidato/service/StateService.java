package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.response.AddStateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
<<<<<<< Updated upstream
=======
import com.busquedaCandidato.candidato.exception.type.StateAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.Request.IMapperStatusRequest;
import com.busquedaCandidato.candidato.mapper.Response.IMapperStatusResponse;
>>>>>>> Stashed changes
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
