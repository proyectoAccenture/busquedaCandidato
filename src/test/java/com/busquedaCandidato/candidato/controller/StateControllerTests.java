package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import com.busquedaCandidato.candidato.service.StateService;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class StateControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IStateRepository stateRepository;

    @Autowired
    private StateService stateService;

    @Test
    @DirtiesContext
    void get_state_by_id_should_return_200(){
        StateEntity stateEntity = new StateEntity(null, "Nuevo");
        StateEntity saveEntity = stateRepository.save(stateEntity);

        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/state/" + saveEntity.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nuevo", response.getBody().getName());
    }


    @Test
    @DirtiesContext
    void get_all_states_should_return_200() {
        stateRepository.save(new StateEntity(null, "En prueba tecnica"));
        stateRepository.save(new StateEntity(null, "Pasó prueba tecnica"));

        ResponseEntity<List<StateResponseDto>> response = restTemplate.exchange(
                "/api/state/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        List<String> stateNames = response.getBody().stream().map(StateResponseDto::getName).toList();
        assertTrue(stateNames.contains("En prueba tecnica"));
        assertTrue(stateNames.contains("Pasó prueba tecnica"));
    }

    @Test
    @DirtiesContext
    void create_state_should_return_201() {
        StateRequestDto requestDto = new StateRequestDto();
        requestDto.setName("Validacion");

        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/state/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validacion", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_state_should_return_200() {
        StateEntity stateEntity = stateRepository.save(new StateEntity(null, "Entrevista cli"));

        StateRequestDto updateRequest = new StateRequestDto();
        updateRequest.setName("Entrevista cliente");

        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/state/" + stateEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Entrevista cliente", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void delete_state_should_return_204() {
        StateEntity stateEntity = stateRepository.save(new StateEntity(null, "Asignacion salarial"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/state/" + stateEntity.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(stateRepository.existsById(stateEntity.getId()));
    }
}

