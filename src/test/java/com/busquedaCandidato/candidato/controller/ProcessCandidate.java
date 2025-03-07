package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.repository.*;
import com.busquedaCandidato.candidato.service.PostulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usa H2
public class ProcessCandidate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IStateRepository stateRepository;

    @Autowired
    private IPhaseRepository phaseRepository;

    @Autowired
    private ICandidateProcessRepository candidateProcessRepository;

    @Autowired
    private ICandidateRepository candidateRepository;

    @Autowired
    private IProcessRepository processRepository;



    @Test
    @DirtiesContext
    void get_process_by_id_should_return_200() {

        PhaseEntity phase = new PhaseEntity(null, "Validation");
        PhaseEntity phaseSave = phaseRepository.save(phase);

        StateEntity stateEntity = new StateEntity(null, "Nuevo");
        StateEntity stateSave = stateRepository.save(stateEntity);

        CandidateEntity candidate = new CandidateEntity(null, "Juan", "Perez", 12345678L, LocalDate.of(1990, 5, 20), 3001234567L, "Bogotá", "juan.perez@example.com");
        CandidateEntity savedCandidate = candidateRepository.save(candidate);

        // Creamos CandidateProcessEntity sin asignarle aún un ProcessEntity
        CandidateProcessEntity candidateProcessEntity = new CandidateProcessEntity(null, phaseSave, stateSave, "Phase number 1", false, LocalDate.of(1990, 5, 20));
        CandidateProcessEntity savedCandidateProcess = candidateProcessRepository.save(candidateProcessEntity);

        // Creamos ProcessEntity y le asignamos la lista de CandidateProcessEntity
        List<CandidateProcessEntity> candidateProcesses = new ArrayList<>();
        candidateProcesses.add(savedCandidateProcess);

        ProcessEntity process = new ProcessEntity(null, savedCandidate, candidateProcesses);
        ProcessEntity savedProcess = processRepository.save(process);

        ResponseEntity<ProcessEntity> response = restTemplate.exchange(
                "/process/" + savedProcess.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }



}
