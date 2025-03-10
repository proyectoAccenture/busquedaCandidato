/* package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.service.CandidateService;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usa H2 para pruebas
public class CandidateControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ICandidateRepository candidateRepository;

    @Autowired
    private CandidateService candidateService;

    @Test
    @DirtiesContext
    void get_candidate_by_id_should_return_200() {
        CandidateEntity candidateEntity = new CandidateEntity(null, "name", "lastname", 1000880088L, 3002550033L, "city", "email@mail.com", LocalDate.of(2000, 1, 1), LocalDate.of(2025, 1, 1));
        CandidateEntity savedEntity = candidateRepository.save(candidateEntity);

        ResponseEntity<CandidateResponseDto> response = restTemplate.exchange(
                "/api/candidate/id/" + savedEntity.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Verificar respuestas
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("name", response.getBody().getName());
        assertEquals("lastname", response.getBody().getLastName());
    }

    @Test
    @DirtiesContext
    void get_all_candidates_should_return_200() {
        CandidateEntity candidateEntity1 = new CandidateEntity(null, "name1", "lastname1", 1000880087L, 3002550031L, "city", "email@mail.com", LocalDate.of(2000, 1, 1), LocalDate.of(2025, 1, 1));

        CandidateEntity candidateEntity2 = new CandidateEntity(null, "name2", "lastname2", 1000880088L, 3002550033L, "city", "email@mail.com", LocalDate.of(2000, 1, 1), LocalDate.of(2025, 1, 1));


        ResponseEntity<List<CandidateResponseDto>> response = restTemplate.exchange(
                "/api/candidate/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        List<String> candidateNames = response.getBody().stream().map(CandidateResponseDto::getName).toList();
        assertTrue(candidateNames.contains("name1"));
        assertTrue(candidateNames.contains("name2"));
    }

    @Test
    @DirtiesContext
    void create_candidate_should_return_201() {

        CandidateRequestDto requestDto = new CandidateRequestDto();
        requestDto.setName("David");
        requestDto.setLastName("Williams");
        requestDto.setCard(123459L);
        requestDto.setPhone(9876543213L);
        requestDto.setCity("CityW");
        requestDto.setEmail("david.williams@example.com");
        requestDto.setBirthdate(LocalDate.of(1995, 4, 10));
        requestDto.setRegistrationDate(LocalDate.of(1995, 4, 10));

        ResponseEntity<CandidateResponseDto> response = restTemplate.exchange(
                "/api/candidate/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("David", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_candidate_should_return_200() {
        CandidateEntity candidateEntity1 = new CandidateEntity(null, "name1", "lastname1", 1000880087L, 3002550031L, "city", "email@mail.com", LocalDate.of(2000, 1, 1), LocalDate.of(2025, 1, 1));
        CandidateEntity candidateSave = new CandidateEntity();


        CandidateRequestDto requestDto = new CandidateRequestDto();
        requestDto.setName("David");
        requestDto.setLastName("Williams");
        requestDto.setCard(123459L);
        requestDto.setPhone(9876543213L);
        requestDto.setCity("CityW");
        requestDto.setEmail("david.williams@example.com");
        requestDto.setBirthdate(LocalDate.of(1995, 4, 10));
        requestDto.setRegistrationDate(LocalDate.of(1995, 4, 10));

        ResponseEntity<CandidateResponseDto> response = restTemplate.exchange(
                "/candidate/" + candidateEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Michael Updated", response.getBody().getName());
        assertEquals("Brown Updated", response.getBody().getLastName());
    }

    @Test
    @DirtiesContext
    void delete_candidate_should_return_204() {
        CandidateEntity candidateEntity = candidateRepository.save(new CandidateEntity(
                null, "N5", "LN5", 123461L,
                LocalDate.of(1995, 5, 25), 9876543215L, "CityV", "N5@mail.com"
        ));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/candidate/" + candidateEntity.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(candidateRepository.existsById(candidateEntity.getId()));
    }
}

 */