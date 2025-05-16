package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.StateRequestDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.repository.IStateRepository;
import com.candidateSearch.searching.service.StateService;
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
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
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
        StateEntity stateEntity = new StateEntity(1L, "state");
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
        assertEquals("state", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_state_should_return_200() {
        StateEntity stateEntity = stateRepository.save(new StateEntity(1L, "state"));

        StateRequestDto updateRequest = new StateRequestDto();
        updateRequest.setName("state new");

        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/state/" + stateEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("state new", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void delete_state_should_return_204() {
        StateEntity stateEntity = stateRepository.save(new StateEntity(1L, "state"));

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

