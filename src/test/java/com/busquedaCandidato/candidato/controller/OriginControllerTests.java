package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import com.busquedaCandidato.candidato.service.OriginService;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usa H2
public class OriginControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IOriginRepository originRepository;

    @Autowired
    private OriginService originService;

    @Test
    @DirtiesContext
    void get_origin_by_id_should_return_200(){
        OriginEntity originEntity = new OriginEntity(null, "origin");
        OriginEntity originSave = originRepository.save(originEntity);

        ResponseEntity<OriginResponseDto> response = restTemplate.exchange(
                "/api/origin/" + originSave.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("origin", response.getBody().getName());
    }


    @Test
    @DirtiesContext
    void get_all_origin_should_return_200() {
        originRepository.save(new OriginEntity(null, "origen 1"));
        originRepository.save(new OriginEntity(null, "origen 2"));


        ResponseEntity<List<OriginResponseDto>> response = restTemplate.exchange(
                "/api/origin/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        List<String> stateNames = response.getBody().stream().map(OriginResponseDto::getName).toList();
        assertTrue(stateNames.contains("origen 1"));
        assertTrue(stateNames.contains("origen 2"));
    }

    @Test
    @DirtiesContext
    void create_origin_should_return_201() {
        OriginRequestDto requestDto = new OriginRequestDto();
        requestDto.setName("origen");

        ResponseEntity<OriginResponseDto> response = restTemplate.exchange(
                "/api/origin/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("origen", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_origin_should_return_200() {
        OriginEntity stateEntity = originRepository.save(new OriginEntity(null, "Origin"));

        StateRequestDto updateRequest = new StateRequestDto();
        updateRequest.setName("Origin new");

        ResponseEntity<OriginResponseDto> response = restTemplate.exchange(
                "/api/origin/" + stateEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Origin new", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void delete_origin_should_return_204() {
        OriginEntity originEntity = originRepository.save(new OriginEntity(null, "Asignacion salarial"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/origin/" + originEntity.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(originRepository.existsById(originEntity.getId()));
    }
}
